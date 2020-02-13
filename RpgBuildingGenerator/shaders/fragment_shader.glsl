#version 410 

in vec3 Colour;
out vec4 FragColour;

/*
in float highlight;
out vec4 FragColour;

void main(){
    vec3 colour;
    colour = vec3(100,100,100);
    
    if (highlight == 1.0)
    {
        colour = vec3(255,255,255);
    }
    else
    {
        colour = vec3(100,100,100);
    }
    FragColour = vec4(colour, 1.0);
} */

void main(){
    FragColour = vec4(Colour, 1.0);
}
