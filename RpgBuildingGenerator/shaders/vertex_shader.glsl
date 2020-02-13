#version 410


layout (location = 0) in vec3 VertexPosition;
layout (location = 1) in vec3 VertexColour;
uniform mat4 Transform;
out vec3 Colour;

void main(){
    Colour = VertexColour;
    gl_Position = Transform * vec4(VertexPosition, 1.0);
    // gl_Position = vec4(VertexPosition, 1.0);
} 