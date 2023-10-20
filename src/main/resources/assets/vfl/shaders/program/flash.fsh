#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;

in vec2 texCoord;

out vec4 fragColor;

void main(){
    vec2 uv = (texCoord) * 2 - 1;
    float distance = length(uv);
    vec3 col = texture2D(DiffuseSampler, texCoord).rgb;
    if (distance < 0.5) {
        col = texture2D(DiffuseSampler, texCoord).rgb * 2;
    }
    fragColor = vec4(col, 1.0);
}
