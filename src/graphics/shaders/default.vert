varying vec3 color;
uniform float lighting;
uniform float faceBrightness;
uniform vec3 lightPosition;

void main()
{
	gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;

	if(lighting != 0f)
	{
		vec3 vertexPosition = (gl_ModelViewMatrix * gl_Vertex).xyz;
		
		vec3 lightDirection = normalize(lightPosition.xyz - vertexPosition);    
 		vec3 surfaceNormal = (gl_NormalMatrix * gl_Normal).xyz;
    
		float diffuseLightIntensity = max(0.0, dot(surfaceNormal, lightDirection));
    
		color.rgb = diffuseLightIntensity * gl_FrontMaterial.diffuse.rgb;
 		color += gl_FrontMaterial.ambient.rgb + gl_LightModel.ambient.rgb;
 		color += faceBrightness;
    
		vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
		
		float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
    
		if(diffuseLightIntensity != 0.0)
		{
			float fspecular = pow(specular, gl_FrontMaterial.shininess);
			color.rgb += (gl_FrontMaterial.specular.rgb * fspecular);
		}
    }
    else
    {
    	color.rgb = gl_FrontMaterial.diffuse.rgb;
    }
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}