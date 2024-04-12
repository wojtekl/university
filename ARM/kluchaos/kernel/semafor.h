// to jest tylko szkielet
#ifndef SEMAFOR_H
#define SEMAFOR_H

typedef unsigned char* semafor;

__asm semafor semafor_stworz(void);
__asm void semafor_wez(semafor, const unsigned int);
__asm void semafor_daj(semafor);

#endif
