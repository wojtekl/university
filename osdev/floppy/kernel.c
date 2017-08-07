#include "common.h"
#include "terminal.h"
#include "terminal_backend_b8000.h"

unsigned long long k;

void _start(void* kernel_location)
{
  UNUSED(kernel_location);
  
  TerminalBackend *con = TerminalBackendB8000();
  
  T_ClearScreen(con);
  T_PutText(con, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\t\tXXXXXXX\n");
  T_Printf(con, "%p %x %i %s %c %u\n", con, 0x41424344, -1234LL, "alamakota", 'X', 1234567890123LL);
  
  char *textvram = (char*)0xB8000;
  
  unsigned long long x = 0x345678;
  unsigned long long y = 0x245678;
  
  int *ptr_x = (int*)x;
  int *ptr_y = (int*)y;
  
  *ptr_x = 0x41414141;
  
  UNUSED(ptr_y);
  
  // unsigned long long addr = (unsigned long long)*ptr_y;
  
  // unsigned long long addr = (unsigned long long)kernel_location;
  unsigned long long addr = (unsigned long long)k;
  for(int i = 0; i < 16; ++i)
  {
    textvram[i * 2] = "0123456789ABCDEF"[(addr >> 60) & 0xf];
    addr <<= 4;
  }
  
  //*(long long*)0xb8000 = 0x4141414141414141LL;
  
  for(;;);
}

