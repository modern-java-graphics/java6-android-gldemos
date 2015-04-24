#version 300 es
/**
 * Copyright 2015 Michael Leahy / TyphonRT, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Sourced from GPUImage which:
// Sourced from Kyprianidis, J. E., Kang, H., and Doellner, J. "Anisotropic Kuwahara Filtering on the GPU,"
// GPU Pro p.247 (2010).
//
// Original header:
//
// Anisotropic Kuwahara Filtering on the GPU
// by Jan Eric Kyprianidis <www.kyprianidis.com>

precision highp float;
in vec2 vTexCoord;

uniform sampler2D uSourceTex; //0

uniform vec2 sampleStep;

out vec4 outColor;

void main()
{
   vec2 uv = vTexCoord;

   vec4 originalColor = texture(uSourceTex, uv);

   float n = float(16); // radius is assumed to be 3
   vec3 m0 = vec3(0.0); vec3 m1 = vec3(0.0); vec3 m2 = vec3(0.0); vec3 m3 = vec3(0.0);
   vec3 s0 = vec3(0.0); vec3 s1 = vec3(0.0); vec3 s2 = vec3(0.0); vec3 s3 = vec3(0.0);
   vec3 c;
   vec3 cSq;

   c = texture(uSourceTex, uv + vec2(-3,-3) * sampleStep).rgb;
   m0 += c;
   s0 += c * c;
   c = texture(uSourceTex, uv + vec2(-3,-2) * sampleStep).rgb;
   m0 += c;
   s0 += c * c;
   c = texture(uSourceTex, uv + vec2(-3,-1) * sampleStep).rgb;
   m0 += c;
   s0 += c * c;
   c = texture(uSourceTex, uv + vec2(-3,0) * sampleStep).rgb;
   cSq = c * c;
   m0 += c;
   s0 += cSq;
   m1 += c;
   s1 += cSq;

   c = texture(uSourceTex, uv + vec2(-2,-3) * sampleStep).rgb;
   m0 += c;
   s0 += c * c;
   c = texture(uSourceTex, uv + vec2(-2,-2) * sampleStep).rgb;
   m0 += c;
   s0 += c * c;
   c = texture(uSourceTex, uv + vec2(-2,-1) * sampleStep).rgb;
   m0 += c;
   s0 += c * c;
   c = texture(uSourceTex, uv + vec2(-2,0) * sampleStep).rgb;
   cSq = c * c;
   m0 += c;
   s0 += cSq;
   m1 += c;
   s1 += cSq;

   c = texture(uSourceTex, uv + vec2(-1,-3) * sampleStep).rgb;
   m0 += c;
   s0 += c * c;
   c = texture(uSourceTex, uv + vec2(-1,-2) * sampleStep).rgb;
   m0 += c;
   s0 += c * c;
   c = texture(uSourceTex, uv + vec2(-1,-1) * sampleStep).rgb;
   m0 += c;
   s0 += c * c;
   c = texture(uSourceTex, uv + vec2(-1,0) * sampleStep).rgb;
   cSq = c * c;
   m0 += c;
   s0 += cSq;
   m1 += c;
   s1 += cSq;

   c = texture(uSourceTex, uv + vec2(0,-3) * sampleStep).rgb;
   cSq = c * c;
   m0 += c;
   s0 += cSq;
   m3 += c;
   s3 += cSq;
   c = texture(uSourceTex, uv + vec2(0,-2) * sampleStep).rgb;
   cSq = c * c;
   m0 += c;
   s0 += cSq;
   m3 += c;
   s3 += cSq;
   c = texture(uSourceTex, uv + vec2(0,-1) * sampleStep).rgb;
   cSq = c * c;
   m0 += c;
   s0 += cSq;
   m3 += c;
   s3 += cSq;
   c = texture(uSourceTex, uv + vec2(0,0) * sampleStep).rgb;
   cSq = c * c;
   m0 += c;
   s0 += cSq;
   m1 += c;
   s1 += cSq;
   m2 += c;
   s2 += cSq;
   m3 += c;
   s3 += cSq;

   c = texture(uSourceTex, uv + vec2(-3,3) * sampleStep).rgb;
   m1 += c;
   s1 += c * c;
   c = texture(uSourceTex, uv + vec2(-3,2) * sampleStep).rgb;
   m1 += c;
   s1 += c * c;
   c = texture(uSourceTex, uv + vec2(-3,1) * sampleStep).rgb;
   m1 += c;
   s1 += c * c;

   c = texture(uSourceTex, uv + vec2(-2,3) * sampleStep).rgb;
   m1 += c;
   s1 += c * c;
   c = texture(uSourceTex, uv + vec2(-2,2) * sampleStep).rgb;
   m1 += c;
   s1 += c * c;
   c = texture(uSourceTex, uv + vec2(-2,1) * sampleStep).rgb;
   m1 += c;
   s1 += c * c;

   c = texture(uSourceTex, uv + vec2(-1,3) * sampleStep).rgb;
   m1 += c;
   s1 += c * c;
   c = texture(uSourceTex, uv + vec2(-1,2) * sampleStep).rgb;
   m1 += c;
   s1 += c * c;
   c = texture(uSourceTex, uv + vec2(-1,1) * sampleStep).rgb;
   m1 += c;
   s1 += c * c;

   c = texture(uSourceTex, uv + vec2(0,3) * sampleStep).rgb;
   cSq = c * c;
   m1 += c;
   s1 += cSq;
   m2 += c;
   s2 += cSq;
   c = texture(uSourceTex, uv + vec2(0,2) * sampleStep).rgb;
   cSq = c * c;
   m1 += c;
   s1 += cSq;
   m2 += c;
   s2 += cSq;
   c = texture(uSourceTex, uv + vec2(0,1) * sampleStep).rgb;
   cSq = c * c;
   m1 += c;
   s1 += cSq;
   m2 += c;
   s2 += cSq;

   c = texture(uSourceTex, uv + vec2(3,3) * sampleStep).rgb;
   m2 += c;
   s2 += c * c;
   c = texture(uSourceTex, uv + vec2(3,2) * sampleStep).rgb;
   m2 += c;
   s2 += c * c;
   c = texture(uSourceTex, uv + vec2(3,1) * sampleStep).rgb;
   m2 += c;
   s2 += c * c;
   c = texture(uSourceTex, uv + vec2(3,0) * sampleStep).rgb;
   cSq = c * c;
   m2 += c;
   s2 += cSq;
   m3 += c;
   s3 += cSq;

   c = texture(uSourceTex, uv + vec2(2,3) * sampleStep).rgb;
   m2 += c;
   s2 += c * c;
   c = texture(uSourceTex, uv + vec2(2,2) * sampleStep).rgb;
   m2 += c;
   s2 += c * c;
   c = texture(uSourceTex, uv + vec2(2,1) * sampleStep).rgb;
   m2 += c;
   s2 += c * c;
   c = texture(uSourceTex, uv + vec2(2,0) * sampleStep).rgb;
   cSq = c * c;
   m2 += c;
   s2 += cSq;
   m3 += c;
   s3 += cSq;

   c = texture(uSourceTex, uv + vec2(1,3) * sampleStep).rgb;
   m2 += c;
   s2 += c * c;
   c = texture(uSourceTex, uv + vec2(1,2) * sampleStep).rgb;
   m2 += c;
   s2 += c * c;
   c = texture(uSourceTex, uv + vec2(1,1) * sampleStep).rgb;
   m2 += c;
   s2 += c * c;
   c = texture(uSourceTex, uv + vec2(1,0) * sampleStep).rgb;
   cSq = c * c;
   m2 += c;
   s2 += cSq;
   m3 += c;
   s3 += cSq;

   c = texture(uSourceTex, uv + vec2(3,-3) * sampleStep).rgb;
   m3 += c;
   s3 += c * c;
   c = texture(uSourceTex, uv + vec2(3,-2) * sampleStep).rgb;
   m3 += c;
   s3 += c * c;
   c = texture(uSourceTex, uv + vec2(3,-1) * sampleStep).rgb;
   m3 += c;
   s3 += c * c;

   c = texture(uSourceTex, uv + vec2(2,-3) * sampleStep).rgb;
   m3 += c;
   s3 += c * c;
   c = texture(uSourceTex, uv + vec2(2,-2) * sampleStep).rgb;
   m3 += c;
   s3 += c * c;
   c = texture(uSourceTex, uv + vec2(2,-1) * sampleStep).rgb;
   m3 += c;
   s3 += c * c;

   c = texture(uSourceTex, uv + vec2(1,-3) * sampleStep).rgb;
   m3 += c;
   s3 += c * c;
   c = texture(uSourceTex, uv + vec2(1,-2) * sampleStep).rgb;
   m3 += c;
   s3 += c * c;
   c = texture(uSourceTex, uv + vec2(1,-1) * sampleStep).rgb;
   m3 += c;
   s3 += c * c;

   float min_sigma2 = 1e+2;
   m0 /= n;
   s0 = abs(s0 / n - m0 * m0);

   float sigma2 = s0.r + s0.g + s0.b;
   if (sigma2 < min_sigma2)
   {
      min_sigma2 = sigma2;
      outColor = vec4(m0, originalColor.a);
   }

   m1 /= n;
   s1 = abs(s1 / n - m1 * m1);

   sigma2 = s1.r + s1.g + s1.b;
   if (sigma2 < min_sigma2)
   {
      min_sigma2 = sigma2;
      outColor = vec4(m1, originalColor.a);
   }

   m2 /= n;
   s2 = abs(s2 / n - m2 * m2);

   sigma2 = s2.r + s2.g + s2.b;
   if (sigma2 < min_sigma2)
   {
      min_sigma2 = sigma2;
      outColor = vec4(m2, originalColor.a);
   }

   m3 /= n;
   s3 = abs(s3 / n - m3 * m3);

   sigma2 = s3.r + s3.g + s3.b;
   if (sigma2 < min_sigma2)
   {
      min_sigma2 = sigma2;
      outColor = vec4(m3, originalColor.a);
   }
}


