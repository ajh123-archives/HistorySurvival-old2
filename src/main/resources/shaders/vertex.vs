#version 400 core

in vec3 pos;
in vec2 tccs;

out vec2 ftccs;

uniform mat4 transMatrix;

void main(){
    gl_Position = transMatrix * vec4(pos, 1.0);
    ftccs = tccs;
}