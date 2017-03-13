#include <stdio.h>
#include "x86_64-linux/include/CL/cl.h"

int main()
{
    
    const char *source = 
"__kernel void Add(__global int *a, __global int *b, __global int *c, int size)\
{\
    int n = get_global_id(0);\
    if(n < size)\
    {\
        c[n] = a[n] + b[n];\
    }\
}";
    size_t programSize = 176;
    
    cl_int error = CL_SUCCESS;
    cl_uint platformNumber = 0;
    error = clGetPlatformIDs(0, NULL, &platformNumber);
    cl_platform_id *platformIds = malloc(sizeof(cl_platform_id) * platformNumber);
    error = clGetPlatformIDs(platformNumber, platformIds, NULL);
    cl_uint i;
    for(i = 0; i < platformNumber; ++i)
    {
        char name[1024] = { '\0' };
        error = clGetPlatformInfo(*(platformIds + i), CL_PLATFORM_VERSION, 1024, &name, NULL);
        printf("Platforma: %s\n", name);
    }
    
    cl_uint deviceNumber;
    error = clGetDeviceIDs(*platformIds, CL_DEVICE_TYPE_GPU, 0, NULL, &deviceNumber);
    cl_device_id *deviceIds = malloc(sizeof(cl_device_id) * deviceNumber);
    error = clGetDeviceIDs(*platformIds, CL_DEVICE_TYPE_GPU, deviceNumber, deviceIds, &deviceNumber);
    for(i = 0; i < deviceNumber; ++i)
    {
        char name[1024] = { '\0' };
        error = clGetDeviceInfo(*(deviceIds + i), CL_DEVICE_VERSION, 1024, &name, NULL);
        printf("Urządzenie: %s\n", name);
    }
    
    size_t vectorSize = 32;
    cl_int *a = malloc(sizeof(cl_int) * vectorSize);
    cl_int *b = malloc(sizeof(cl_int) * vectorSize);
    cl_int *c = malloc(sizeof(cl_int) * vectorSize);
    for(i = 0; i < vectorSize; ++i)
    {
        *(a + i) = 1;
        *(b + i) = 2;
    }
    
    cl_context context = clCreateContext(0, deviceNumber, deviceIds, NULL, NULL, NULL);
    if(NULL == context)
    {
        printf("blad");
    }
    cl_command_queue commandQueue = clCreateCommandQueue(context, *deviceIds, 0, &error);
    cl_mem bufferA = clCreateBuffer(context, CL_MEM_READ_ONLY, sizeof(cl_int) * vectorSize, NULL, &error);
    cl_mem bufferB = clCreateBuffer(context, CL_MEM_READ_ONLY, sizeof(cl_int) * vectorSize, NULL, &error);
    cl_mem bufferC = clCreateBuffer(context, CL_MEM_WRITE_ONLY, sizeof(cl_int) * vectorSize, NULL, &error);
    cl_program program = clCreateProgramWithSource(context, 1, &source, &programSize, &error);
    error = clBuildProgram(program, 0, NULL, NULL, NULL, NULL);
    cl_kernel kernel = clCreateKernel(program, "Add", &error);
    error = clSetKernelArg(kernel, 0, sizeof(cl_mem), (void*)&bufferA);
    error = clSetKernelArg(kernel, 1, sizeof(cl_mem), (void*)&bufferB);
    error = clSetKernelArg(kernel, 2, sizeof(cl_mem), (void*)&bufferC);
    error = clSetKernelArg(kernel, 3, sizeof(cl_int), (void*)&vectorSize);
    error = clEnqueueWriteBuffer(commandQueue, bufferA, CL_FALSE, 0, sizeof(cl_int) * vectorSize, a, 0, NULL, NULL);
    error = clEnqueueWriteBuffer(commandQueue, bufferB, CL_FALSE, 0, sizeof(cl_int) * vectorSize, b, 0, NULL, NULL);
    size_t localWorkSize = 1;
    error = clEnqueueNDRangeKernel(commandQueue, kernel, 1, NULL, &vectorSize, &localWorkSize, 0, NULL, NULL);
    error = clEnqueueReadBuffer(commandQueue, bufferC, CL_TRUE, 0, sizeof(cl_int) * vectorSize, c, 0, NULL, NULL);
    clFlush(commandQueue);
    clFinish(commandQueue);
    clReleaseKernel(kernel);
    clReleaseProgram(program);
    clReleaseMemObject(bufferA);
    clReleaseMemObject(bufferB);
    clReleaseMemObject(bufferC);
    clReleaseCommandQueue(commandQueue);
    clReleaseContext(context);
    
    for(i = 0; i < vectorSize; ++i)
    {
        printf("[%d]=%d\t", i, *(c + i));
    }
    
    free(deviceIds);
    free(platformIds);
    free(a);
    free(b);
    free(c);
    
    return 0;
    
}
