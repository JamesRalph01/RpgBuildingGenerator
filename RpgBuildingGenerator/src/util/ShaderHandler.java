/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.jogamp.opengl.GL4;


public class ShaderHandler {
    
    public static int createShader(String shaderPath, int shaderType, GL4 gl) {
        
        // Create shader object
        int shader = gl.glCreateShader(shaderType);
        
        if (shader == 0) {
            System.err.println("Error creating shader");
            System.exit(1);
        }
        
        // Copy shader source code into shader object
        String shaderCode = ShaderLoader.loadShaderFile(shaderPath);
        gl.glShaderSource(shader, 1, new String[]{shaderCode}, null);
        
        // Copile the Shader
        gl.glCompileShader(shader);
        
        // Verify shader compiled successfully
        int result[] = new int[1];
        gl.glGetShaderiv(shader, GL4.GL_COMPILE_STATUS, result, 0);
        
        if (result[0] == GL4.GL_FALSE) {
            System.err.println("Shader compilation failed: " + shaderPath);
            
            // Get log of shader compilation
            int logLength[] = new int[1];
            gl.glGetShaderiv(shader, GL4.GL_INFO_LOG_LENGTH, logLength, 0);
            
            if (logLength[0] > 0) {
                byte[] log = new byte[logLength[0]];
                gl.glGetShaderInfoLog(shader, logLength[0], (int[])null, 0, log, 0);
                System.out.println("Shader log: " + new String(log));
            }
            
            System.exit(1); // Terminate in the event of an error
        }
         
        return shader;
    }
    
    public static int createProgram(int shaderList[], GL4 gl) {
        
        int programHandle = gl.glCreateProgram();
        
        if (programHandle == 0) {
            System.err.println("Error creating program object");
            System.exit(1);
        }
        
        for (int shader : shaderList) {
            gl.glAttachShader(programHandle, shader);
        }
        
        return programHandle;
    }
    
    public static void linkProgram(int programHandle, GL4 gl) {
        
        gl.glLinkProgram(programHandle);
        
        int status[] = new int[1];
        gl.glGetProgramiv(programHandle, GL4.GL_LINK_STATUS, status, 0);
        
        if (status[0] == GL4.GL_FALSE) {
            System.err.println("Failed to link shader program");
            int logLength[] = new int[1];
            gl.glGetProgramiv(programHandle, GL4.GL_INFO_LOG_LENGTH, logLength, 0);
            
            if (logLength[0] > 1) {
                byte[] log = new byte[logLength[0]];
                gl.glGetProgramInfoLog(programHandle, logLength[0], (int[])null, 0, log, 0);
                System.out.println("Program Log: " + new String(log));
            }
            System.exit(1);
        }
    }
}
