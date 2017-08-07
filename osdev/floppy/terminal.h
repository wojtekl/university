#pragma once
#include <stdint.h>

struct TerminalBackend;
typedef struct TerminalBackend TerminalBackend;

typedef void (*TBfunc_SetCursorPosition)(TerminalBackend *tb, uint16_t x, uint16_t y);
typedef void (*TBfunc_GetCursorPosition)(TerminalBackend *tb, uint16_t *x, uint16_t *y);
typedef void (*TBfunc_ClearScreen)(TerminalBackend *tb);
typedef void (*TBfunc_PutCharacter)(TerminalBackend *tb, uint32_t ch);
typedef void (*TBfunc_GetSize)(TerminalBackend *tb, uint16_t *w, uint16_t *h);

struct TerminalBackend
{
  TBfunc_SetCursorPosition func_set_cursor_position;
  TBfunc_GetCursorPosition func_get_cursor_position;
  TBfunc_ClearScreen func_clear_screen;
  TBfunc_PutCharacter func_put_character;
  TBfunc_GetSize func_get_size;
  void *user_data;
};

void T_SetCursorPosition(TerminalBackend *tb, unsigned short x, unsigned short y);
void T_GetCursorPosition(TerminalBackend *tb, uint16_t *x, uint16_t *y);

void T_PutText(TerminalBackend *tb, const char *s);
void T_ClearScreen(TerminalBackend *tb);
void T_PutCharacter(TerminalBackend *tb, uint32_t ch);
void T_GetSize(TerminalBackend *tb, uint16_t *w, uint16_t *h);

