
#define UNUSED(a) (void)(a);

enum
{
  kVGAControlRegisterIndex_CursorLocationHigh = 0xe,
  kVGAControlRegisterIndex_CursorLocationLow = 0xf
};

enum
{
  kPortVGAControlRegisterIndex = 0x3d4,
  kPortVGAControlDataRegister = 0x3d5
};

void HAL_PortOutByte(int port, unsigned char v)
{
  __asm("out dx, al\n" : : "a" (v), "d" (port));
}

void HAL_PortOutWord(int port, unsigned short v)
{
  __asm("out dx, ax\n" : : "a" (v), "d" (port));
}

void HAL_PortOutDword(int port, unsigned int v)
{
  __asm("out dx, eax\n" : : "a" (v), "d" (port));
}

struct B8000_ContextStruct
{
  unsigned short x;
  unsigned short y;
}
B8000_Context;

void B8000_SetCursorPosition(unsigned short x, unsigned short y)
{
  unsigned int offset = x + y * 80;
  
  // ASSERT(offset <= 0xffff);
  // ASSERT(offset <= 80 *25);
  
  HAL_PortOutByte(kPortVGAControlRegisterIndex, kVGAControlRegisterIndex_CursorLocationLow);
  HAL_PortOutByte(kPortVGAControlDataRegister, offset & 0xff);
  
  HAL_PortOutByte(kPortVGAControlRegisterIndex, kVGAControlRegisterIndex_CursorLocationHigh);
  HAL_PortOutByte(kPortVGAControlDataRegister, offset >> 8);
  
  B8000_Context.x = x;
  B8000_Context.y = y;
}

void B8000_ClearScreen(void)
{
  unsigned char *textvram = (unsigned char*)0xB8000;
  for(int i = 0; i < 80 * 25; i++)
  {
    textvram[i * 2 + 0] = ' ';
    textvram[i * 2 + 1] = 0x0A;
  }
  
  B8000_SetCursorPosition(0, 0);
}

void B8000_PutCharacter(char ch)
{
  unsigned char *textvram = (unsigned char*)0xB8000;
  
  unsigned short x = B8000_Context.x;
  unsigned short y = B8000_Context.y;
  
  unsigned int offset = x + y * 80;
  textvram[offset * 2 + 0] = ch;
  textvram[offset * 2 + 1] = 0x0A;
  
  x += 1;
  if(80 == x)
  {
    x = 0;
    y += 1;
  }
  
  B8000_SetCursorPosition(x, y);
}

void B8000_PutText(const char *s)
{
  for(; '\0' != *s; s++)
  {
    switch(*s)
    {
      case '\n':
      {
        B8000_SetCursorPosition(0, B8000_Context.y + 1);
        break;
      }
      
      case '\r':
      {
        B8000_SetCursorPosition(0, B8000_Context.y);
        break;
      }
      
      case '\t':
      {
        unsigned short x = B8000_Context.x;
        unsigned short y = B8000_Context.y;
        
        x += 8 - x % 8;
        
        if(80 <= x)
        {
          x = 0;
          y += 1;
          B8000_SetCursorPosition(x, y);
        }
        else
        {
          for(unsigned short i = B8000_Context.x; i < x; i++)
          {
            B8000_PutCharacter(' ');
          }
        }
        break;
      }
      
      default:
        B8000_PutCharacter(*s);
    }
  }
}

void _start(void* kernel_location)
{
  UNUSED(kernel_location);
  
  B8000_ClearScreen();
  B8000_PutText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\t\tXXXXXXX");
  
  char *textvram = (char*)0xB8000;
  unsigned long long addr = (unsigned long long)kernel_location;
  for(int i = 0; i < 16; ++i)
  {
    textvram[i * 2] = "0123456789ABCDEF"[(addr >> 60) & 0xf];
    addr <<= 4;
  }
  
  //*(long long*)0xb8000 = 0x4141414141414141LL;
  
  for(;;);
}

