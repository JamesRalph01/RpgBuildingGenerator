package viewer.engine.graph;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;

    public ShaderProgram(GL4 gl) throws Exception {
        programId = gl.glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
        uniforms = new HashMap<>();
    }

    public void createUniform(GL4 gl, String uniformName) throws Exception {
        int uniformLocation = gl.glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(GL4 gl, String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        value.get(matrixBuffer);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            gl.glUniformMatrix4fv(uniforms.get(uniformName), 1, false, matrixBuffer);
        }
    }

    public void setUniform(GL4 gl, String uniformName, int value) {
        gl.glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(GL4 gl, String uniformName, Vector3f value) {
        gl.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void createVertexShader(GL4 gl, String shaderCode) throws Exception {
        vertexShaderId = createShader(gl, shaderCode, GL4.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(GL4 gl, String shaderCode) throws Exception {
        fragmentShaderId = createShader(gl, shaderCode, GL4.GL_FRAGMENT_SHADER);
    }

    protected int createShader(GL4 gl, String shaderCode, int shaderType) throws Exception {
        int shaderId = gl.glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        String[] sources = new String[]{ shaderCode };
        gl.glShaderSource(shaderId, 1, sources, null);
        gl.glCompileShader(shaderId);

        int logLength[] = new int[1];
        gl.glGetShaderiv(shaderId, GL4.GL_INFO_LOG_LENGTH, logLength, 0);
        if (logLength[0] > 0) {
            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(shaderId, logLength[0], (int[])null, 0, log, 0);
            System.out.println("Shader log: " + new String(log));
            throw new Exception("Error compiling Shader code");
        }
        

        gl.glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link(GL4 gl) throws Exception {
               
        gl.glLinkProgram(programId);
        
        int status[] = new int[1];
        gl.glGetProgramiv(programId, GL4.GL_LINK_STATUS, status, 0);       
        if (status[0] == GL4.GL_FALSE) {
            int logLength[] = new int[1];
            byte[] log = new byte[logLength[0]];
            gl.glGetProgramInfoLog(programId, logLength[0], (int[])null, 0, log, 0);
            System.out.println("Program Log: " + new String(log));
            throw new Exception("Error linking Shader code: ");
        }

        if (vertexShaderId != 0) {
            gl.glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            gl.glDetachShader(programId, fragmentShaderId);
        }

        gl.glValidateProgram(programId);
        gl.glGetProgramiv(programId, GL4.GL_VALIDATE_STATUS, status, 0);  
//        if (status[0] == GL4.GL_FALSE) {
//            int logLength[] = new int[1];
//            byte[] log = new byte[logLength[0]];
//            gl.glGetProgramInfoLog(programId, logLength[0], (int[])null, 0, log, 0);
//            System.out.println("Program Log: " + new String(log));
//            System.err.println("Warning validating Shader code: ");
//        }
    }

    public void bind(GL4 gl) {
        gl.glUseProgram(programId);
    }

    public void unbind(GL4 gl) {
        gl.glUseProgram(0);
    }

    public void cleanup(GL4 gl) {
        unbind(gl);
        if (programId != 0) {
            gl.glDeleteProgram(programId);
        }
    }
}
