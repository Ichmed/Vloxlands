varying vec3 color;
uniform sampler2D tex;

void main()
{
    gl_FragColor = vec4(color, gl_FrontMaterial.diffuse.a);
}