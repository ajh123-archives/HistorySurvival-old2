#version 400 core

in vec3 pos;
in vec2 tccs;

out vec2 ftccs;

void main(){
    gl_Position = vec4(pos, 1.0);
    ftccs = tccs;
}