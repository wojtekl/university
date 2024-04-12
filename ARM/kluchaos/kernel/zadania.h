#ifndef ZADANIA_H
#define ZADANIA_H
void planista_uruchom(void);

void zadanie_dodaj(const unsigned char, const unsigned int, void (*)(void) , const char*);

void zadanie_usun(const unsigned int, const unsigned int);

//void zadanie_uspij(const unsigned int);

//void zadanie_obudz(const unsigned int);

void sprzet_ustaw(void);

void czekaj_us(const unsigned int);

void czekaj_ms(const unsigned int);

void czekaj_s(const unsigned int);
#endif
