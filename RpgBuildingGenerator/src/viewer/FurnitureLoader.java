/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import com.jogamp.opengl.GL4;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import viewer.objutil.FloatTuple;
import viewer.objutil.Mtl;
import viewer.objutil.MtlReader;
import viewer.objutil.Obj;
import viewer.objutil.ObjData;
import viewer.objutil.ObjReader;
import viewer.objutil.ObjSplitting;
import viewer.objutil.ObjUtils;
import building.BuildingItem;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector4f;

/**
 *
 * @author chrisralph
 */
public class FurnitureLoader {
    
    public FurnitureLoader() {
        
    }
    
    public ArrayList<Mesh> loadFurniture(GL4 gl, BuildingItem item) throws FileNotFoundException, IOException {
        ArrayList<Mesh> meshes = new ArrayList<>();

        System.out.println("Loading OBJ" + item.objFilename);
        // Read an OBJ file
        InputStream objInputStream = new FileInputStream("textures/" + item.rootPath + item.objFilename);
        Obj originalObj = ObjReader.read(objInputStream);
        
        // Convert the OBJ into a "renderable" OBJ. 
        // (See ObjUtils#convertToRenderable for details)
        Obj obj = ObjUtils.convertToRenderable(originalObj);
        
        // The OBJ may refer to multiple MTL files using the "mtllib"
        // directive. Each MTL file may contain multiple materials.
        // Here, all materials (in form of Mtl objects) are collected.
        ArrayList<Mtl> allMtls = new ArrayList<>();
        for (String mtlFileName : obj.getMtlFileNames())
        {
            InputStream mtlInputStream = new FileInputStream("textures/" + item.rootPath  + mtlFileName);
            ArrayList<Mtl> mtls = (ArrayList<Mtl>) MtlReader.read(mtlInputStream);
            allMtls.addAll(mtls);
        }
        
        // Split the OBJ into multiple parts. Each key of the resulting
        // map will be the name of one material. Each value will be 
        // an OBJ that contains the OBJ data that has to be rendered
        // with this material.
        Map<String, Obj> materialGroups = ObjSplitting.splitByMaterialGroups(obj);
        
        for (Entry<String, Obj> entry : materialGroups.entrySet())
        {
            String materialName = entry.getKey();
            Obj materialGroup = entry.getValue();
            
            System.out.println("Material name  : " + materialName);
            System.out.println("Material group : " + materialGroup);            
            
            // Find the MTL that defines the material with the current name
            Mtl mtl = findMtlForName(allMtls, materialName);
            
            // Extract the relevant material properties. These properties can 
            // be used to set up the renderer. For example, they may be passed
            // as uniform variables to a shader
            FloatTuple diffuseColor = mtl.getKd();
            FloatTuple specularColor = mtl.getKs();
            // ...
            
            System.out.println("Diffuse name : " + mtl.getMapKd());
            
            Texture texture;
            try {
                texture = new Texture(gl, "textures/" + item.rootPath + mtl.getMapKd());
                Material material = new Material(texture, (1.0f/255.0f) * mtl.getNs()); // reflectance  
                material.setAmbientColour(new Vector4f(mtl.getKa().getX(), mtl.getKa().getY(), mtl.getKa().getZ(), 1.0f));
                material.setDiffuseColour(new Vector4f(mtl.getKd().getX(), mtl.getKd().getY(), mtl.getKd().getZ(), 1.0f));
                material.setSpecularColour(new Vector4f(mtl.getKs().getX(), mtl.getKs().getY(), mtl.getKs().getZ(), 1.0f));
    
                Mesh mesh = new Mesh(gl, ObjData.getVerticesArray(obj), ObjData.getTexCoordsArray(obj, 2, true), 
                                         ObjData.getNormalsArray(obj), ObjData.getFaceNormalIndicesArray(obj));
                mesh.setMaterial(material);
                meshes.add(mesh);
            } catch (Exception ex) {
                Logger.getLogger(FurnitureLoader.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return meshes;
    }
    
    @SuppressWarnings("unused")

    /**
     * Returns the Mtl from the given sequence that has the given name,
     * or <code>null</code> if no such Mtl can be found
     * 
     * @param mtls The Mtl instances
     * @param name The material name
     * @return The Mtl with the given material name
     */
    private Mtl findMtlForName(Iterable<? extends Mtl> mtls, String name)
    {
        for (Mtl mtl : mtls)
        {
            if (mtl.getName().equals(name))
            {
                return mtl;
            }
        }
        return null;
    }

}
