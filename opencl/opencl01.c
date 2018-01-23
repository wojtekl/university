#include <stdio.h>
#include <time.h>
#include "CL/cl.h"

void wypelnij_losowymi(const size_t d, cl_int * 
  const w)
{
  for(size_t i = 0; i < d; ++i)
  {
    w[i] = (float)rand() / RAND_MAX * (4 - 1) + 1;
  }
}

const char * dodaj = "\
__kernel void dodaj(int d, __global int *a, \
  __global int *b, __global int *c)\
{\
  int i = get_global_id(0);\
  if(i < d)\
  {\
    c[i] = a[i] + b[i];\
  }\
}\
";
const size_t dodajRozmiar = 158;
const char * const dodajNazwa = "dodaj";

int main(const int argc, const char * const * const 
  argv)
{
  cl_int rezultat = CL_SUCCESS;
  cl_uint platformLiczba = 0;
  rezultat = clGetPlatformIDs(0, NULL, 
    &platformLiczba);
  if(1 > platformLiczba)
  {
    printf("Nie znaleziono platform obsługujących \
      OpenCL!\n");
    return 0;
  }
  cl_platform_id * const platformIDs = malloc(
    platformLiczba * sizeof(cl_platform_id));
  rezultat = clGetPlatformIDs(platformLiczba, 
    platformIDs, NULL);
  cl_uint * const deviceLiczba = malloc(
    platformLiczba * sizeof(cl_uint));
  cl_device_id * * const deviceIDs = malloc(
    platformLiczba * sizeof(cl_platform_id));
  char tekst[1024] = {'\0'};
  for(cl_uint i = 0; i < platformLiczba; ++i)
  {
    const cl_platform_id * const platformID = 
      &platformIDs[i];
    printf("###\n");
    printf("Platforma %u\n", i);
    rezultat = clGetPlatformInfo(*platformID, 
      CL_PLATFORM_NAME, 1024, tekst, NULL);
    printf("Nazwa: %s\n", tekst);
    rezultat = clGetPlatformInfo(*platformID, 
      CL_PLATFORM_VENDOR, 1024, tekst, NULL);
    printf("Producent: %s\n", tekst);
    printf("---\n");
    
    cl_uint * const w = &deviceLiczba[i];
    rezultat = clGetDeviceIDs(*platformID, 
      CL_DEVICE_TYPE_GPU, 0, NULL, w);
    if(1 > *w)
    {
      printf("Brak urządzeń obsługujących OpenCL na \
        platformie %u!\n", i);
    }
    deviceIDs[i] = malloc(*w * 
      sizeof(cl_device_id));
    rezultat = clGetDeviceIDs(*platformID, 
      CL_DEVICE_TYPE_GPU, *w, deviceIDs[i], w);
    for(cl_uint j = 0; j < *w; ++j)
    {
      const cl_device_id * const deviceID = 
        &deviceIDs[i][j];
      printf("Urządzenie %u\n", j);
      rezultat = clGetDeviceInfo(*deviceID, 
        CL_DEVICE_NAME, 1024, tekst, NULL);
      printf("Nazwa: %s\n", tekst);
      rezultat = clGetDeviceInfo(*deviceID, 
        CL_DEVICE_VENDOR, 1024, tekst, NULL);
      printf("Producent: %s\n", tekst);
      rezultat = clGetDeviceInfo(*deviceID, 
        CL_DEVICE_VERSION, 1024, tekst, NULL);
      printf("Wersja: %s\n", tekst);
      printf("---\n");
    }
    
    printf("###\n\n");
    free(platformIDs);
  }
  
  const size_t rozmiarInt = sizeof(cl_int);
  const size_t rozmiarMem = sizeof(cl_mem);
  const size_t platforma = 0;
  const size_t urzadzenie = 0;
  size_t localWorkSize = 8;
  const size_t d = 32;
  
  cl_int * const a = malloc(d * rozmiarInt);
  wypelnij_losowymi(d, a);
  cl_int * const b = malloc(d * rozmiarInt);
  wypelnij_losowymi(d, b);
  cl_int * const c = malloc(d * rozmiarInt);
  
  const cl_context context = clCreateContext(0, 
    deviceLiczba[platforma], deviceIDs[platforma], 
    NULL, NULL, NULL);
  if(NULL == context)
  {
    printf("Nie utworzono kontekstu urządzenia \
      OpenCL!\n");
    return 0;
  }
  
  const cl_mem bufferA = clCreateBuffer(context, 
    CL_MEM_READ_ONLY, d * rozmiarInt, NULL, 
    &rezultat);
  const cl_mem bufferB = clCreateBuffer(context, 
    CL_MEM_READ_ONLY, d * rozmiarInt, NULL, 
    &rezultat);
  const cl_mem bufferC = clCreateBuffer(context, 
    CL_MEM_WRITE_ONLY, d * rozmiarInt, NULL, 
    &rezultat);
  
  const cl_program program = 
    clCreateProgramWithSource(context, 1, &dodaj, 
    &dodajRozmiar, &rezultat);
  rezultat = clBuildProgram(program, 0, NULL, NULL, 
    NULL, NULL);
  const cl_kernel kernel = clCreateKernel(program, 
    dodajNazwa, &rezultat);
  rezultat = clSetKernelArg(kernel, 0, rozmiarInt, 
    (void*)&d);
  rezultat = clSetKernelArg(kernel, 1, rozmiarMem, 
    (void*)&bufferA);
  rezultat = clSetKernelArg(kernel, 2, rozmiarMem, 
    (void*)&bufferB);
  rezultat = clSetKernelArg(kernel, 3, rozmiarMem, 
    (void*)&bufferC);
  
  const cl_command_queue commandQueue = 
    clCreateCommandQueue(context, 
    deviceIDs[platforma][urzadzenie], 0, &rezultat);
  rezultat = clEnqueueWriteBuffer(commandQueue, 
    bufferA, CL_FALSE, 0, d * rozmiarInt, a, 0, 
    NULL, NULL);
  rezultat = clEnqueueWriteBuffer(commandQueue, 
    bufferB, CL_FALSE, 0, d * rozmiarInt, b, 0, 
    NULL, NULL);
  rezultat = clEnqueueNDRangeKernel(commandQueue, 
    kernel, 1, NULL, &d, &localWorkSize, 0, NULL, 
    NULL);
  rezultat = clEnqueueReadBuffer(commandQueue, 
    bufferC, CL_TRUE, 0, d * rozmiarInt, c, 0, NULL, 
    NULL);
  
  clFlush(commandQueue);
  clFinish(commandQueue);
  clReleaseCommandQueue(commandQueue);
  clReleaseKernel(kernel);
  clReleaseProgram(program);
  clReleaseMemObject(bufferC);
  clReleaseMemObject(bufferB);
  clReleaseMemObject(bufferA);
  clReleaseContext(context);
  
  for(size_t i = 0; i < d; ++i)
  {
    printf("A[%u] + B[%u] = C[%u]: %i + %i = %i\n", 
      (cl_uint)i, (cl_uint)i, (cl_uint)i, a[i], 
      b[i], c[i]);
  }
  
  free(c);
  free(b);
  free(a);
  
  for(cl_uint i = 0; i < platformLiczba; ++i)
  {
    free(deviceIDs[i]);
  }
  free(deviceIDs);
  free(deviceLiczba);
  
  return 0;
}

