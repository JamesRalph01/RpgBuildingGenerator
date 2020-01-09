/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author James
 */
public class ShaderLoader {
    
    public static String loadShaderFile(String filename)
    {
            StringBuilder vertexCode = new StringBuilder();
            String line = null;
            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                while( (line = reader.readLine()) !=null )
                {
                    vertexCode.append(line);
                    vertexCode.append('\n');
                }
            }
            catch(Exception e)
            {
                    throw new IllegalArgumentException("unable to load shader from file ["+filename+"]", e);
            }

            return vertexCode.toString();
    }
}
