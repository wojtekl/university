#define CL_HPP_TARGET_OPENCL_VERSION 120
#define __CL_ENABLE_EXCEPTIONS

#include <iostream>
#include <string>
#include "CL/cl.hpp"

using std::cout;
using std::endl;
using std::vector;
using cl::Platform;
using cl::Device;
using cl::Buffer;

void wypelnij_losowymi(const size_t d, cl_int * 
  const w)
{
  for(size_t i = 0; i < d; ++i)
  {
    w[i] = (float)rand() / RAND_MAX * (4 - 1) + 1;
  }
}

const char * const dodaj = "\
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
  std::string tekst;
  vector<Platform> platforms;
  try
  {
    Platform::get(&platforms);
    vector<vector<Device>> devices(platforms.size());
    vector<vector<Device>>::iterator j = devices
      .begin();
    for(vector<Platform>::iterator i = 
      platforms.begin(); i != platforms.end(); ++i)
    {
      const Platform platform = *i;
      cout << "###" << endl;
      cout << "Platforma " << endl;
      platform.getInfo(CL_PLATFORM_NAME, &tekst);
      cout << "Nazwa: " << tekst << endl;
      platform.getInfo(CL_PLATFORM_VENDOR, &tekst);
      cout << "Producent: " << tekst << endl;
      cout << "---" << endl;
      
      platform.getDevices(CL_DEVICE_TYPE_GPU, &*j);
      for(vector<Device>::iterator k = (*j).begin(); 
        k != (*j).end(); ++k)
      {
        const Device device = *k;
        cout << "Urządzenie " << endl;
        device.getInfo(CL_DEVICE_NAME, &tekst);
        cout << "Nazwa: " << tekst << endl;
        device.getInfo(CL_DEVICE_VENDOR, &tekst);
        cout << "Producent: " << tekst << endl;
        device.getInfo(CL_DEVICE_VERSION, &tekst);
        cout << "Wersja: " << tekst << endl;
        cout << "---" << endl;
      }
    }
    cout << "###" << endl << endl;
    
    const size_t rozmiarInt = sizeof(cl_int);
    const size_t platforma = 0;
    const size_t urzadzenie = 0;
    const size_t localWorkSize = 8;
    const cl_uint d = 32;
    
    cl_int * const a = new cl_int[d];
    wypelnij_losowymi(d, a);
    cl_int * const b = new cl_int[d];
    wypelnij_losowymi(d, b);
    cl_int * const c = new cl_int[d];
    
    cl_context_properties contextProperties[] = {
      CL_CONTEXT_PLATFORM, 
      (cl_context_properties)(platforms[
        platforma])(), 
      0
    };
    const cl::Context context(CL_DEVICE_TYPE_GPU, 
      contextProperties);
    
    const Buffer bufferA = Buffer(context, 
      CL_MEM_READ_ONLY, d * rozmiarInt);
    const Buffer bufferB = Buffer(context, 
      CL_MEM_READ_ONLY, d * rozmiarInt);
    const Buffer bufferC = Buffer(context, 
      CL_MEM_WRITE_ONLY, d * rozmiarInt);
    
    const cl::Program program = cl::Program(context, 
      dodaj);
    program.build(devices[platforma]);
    cl::Kernel kernel(program, dodajNazwa);
    kernel.setArg(0, d);
    kernel.setArg(1, bufferA);
    kernel.setArg(2, bufferB);
    kernel.setArg(3, bufferC);
    
    const cl::CommandQueue commandQueue = 
      cl::CommandQueue(context, devices[platforma][urzadzenie]);
    commandQueue.enqueueWriteBuffer(bufferA, 
      CL_TRUE, 0, d * rozmiarInt, a);
    commandQueue.enqueueWriteBuffer(bufferB, 
      CL_TRUE, 0, d * rozmiarInt, b);
    commandQueue.enqueueNDRangeKernel(kernel, 
      cl::NullRange, cl::NDRange(d), 
      cl::NDRange(localWorkSize));
    commandQueue.enqueueReadBuffer(bufferC, CL_TRUE, 
      0, d * rozmiarInt, c);
    commandQueue.flush();
    commandQueue.finish();
    
    for(size_t i = 0; i < d; ++i)
    {
      cout << "A[" << i << "] + B[" << i << "] = C[" 
        << i << "]: " << a[i] << " + " << b[i] << 
        " = " << c[i] << endl;
    }
    
    delete []c;
    delete []b;
    delete []a;
  }
  catch(const cl::Error error)
  {
    cout << "Błąd nr " << error.err() << 
      " podczas wywołania funkcji " << error.what() 
        << "!" << endl;
  }
  
  return 0;
}

