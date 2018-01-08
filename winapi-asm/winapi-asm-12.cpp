#include <iostream>

using namespace std;

void znajdz_najwieksza(int *lista, int dlugosc)
{
  int najwieksza = 0;
  int i = 0;
  
  /*
  najwieksza = lista[0];
  for(int i = 1; i < dlugosc; ++i)
  {
    if(najwieksza < lista[i])
    {
      najwieksza = lista[i];
    }
  }
  */
  
  asm volatile (
    ".intel_syntax noprefix"
    "\n mov esi, [ebp + 8]" // lista
    "\n mov eax, [esi]"
    "\n mov [ebp - 4], eax" // najwieksza = lista[0]
    "\n mov eax, 4"
    "\n mov [ebp - 8], eax" // i = 1
    "\n petla:"
    "\n   mov eax, [ebp + 12]" // dlugosc
    "\n   mov ecx, 4"
    "\n   mul ecx"
    "\n   mov ecx, [ebp - 8]" // i
    "\n   cmp ecx, eax" // i >= dlugosc
    "\n   jae koniec"
    "\n   mov ebx, [ebp - 4]" // najwieksza
    "\n   mov edx, [ebp + 8]" // lista
    "\n   mov ecx, [ebp - 8]" // i
    "\n   add edx, ecx"
    "\n   mov edx, [edx]"
    "\n   cmp ebx, edx" // najwieksza >= lista[i]
    "\n   jae pomin"
    "\n   mov [ebp - 4], edx" // najwieksza = lista[i]
    "\n pomin:"
    "\n   mov ecx, [ebp - 8]" // i
    "\n   add ecx, 4"
    "\n   mov [ebp - 8], ecx" // ++i
    "\n   jmp petla"
    "\n koniec:"
    "\n .att_syntax"
    "\n "
    :
  );
  
  cout << "Najwieksza wartosc w tym ciagu to: " << najwieksza << endl;
  cout << "Jej adres to: " << &najwieksza << endl;
}

#define DLUGOSC 4

int main(int argc, char **argv)
{
  int lista[DLUGOSC] = {1, 4, 2, 3};
  znajdz_najwieksza(lista, DLUGOSC);
  system("pause");
  return 0;
}
