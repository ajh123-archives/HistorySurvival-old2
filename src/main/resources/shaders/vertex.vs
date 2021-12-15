#version 400 core

in vec3 pos;
in vec2 tccs;

out vec2 ftccs;

uniform mat4 transMatrix;
uniform mat4 projMatrix;
uniform mat4 viewMatrix;

void main(){
    gl_Position = projMatrix * viewMatrix * transMatrix * vec4(pos, 1.0);
    ftccs = tccs;
}