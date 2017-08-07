#include "terminal.h"

void T_SetCursorPosition(TerminalBackend *tb, uint16_t x, uint16_t y)
{
  tb -> func_set_cursor_position(tb, x, y);
}

void T_PutText(TerminalBackend *tb, const char *s)
{
  for(; '\0' != *s; s++)
  {
    switch(*s)
    {
      case '\n':
      {
        uint16_t x, y;
        T_GetCursorPosition(tb, &x, &y);
        T_SetCursorPosition(tb, 0, y + 1);
        break;
      }
      
      case '\r':
      {
        uint16_t x, y;
        T_GetCursorPosition(tb, &x, &y);
        T_SetCursorPosition(tb, 0, y);
        break;
      }
      
      case '\t':
      {
        uint16_t x, y;
        uint16_t sx, sy;
        T_GetCursorPosition(tb, &sx, &sy);
        x = sx;
        y = sy;
        
        uint16_t w, h;
        T_GetSize(tb, &w, &h);
        
        x += 8 - x % 8;
        
        if(w <= x)
        {
          x = 0;
          y += 1;
          T_SetCursorPosition(tb, x, y);
        }
        else
        {
          for(unsigned short i = sx; i < x; i++)
          {
            T_PutCharacter(tb, ' ');
          }
        }
        break;
      }
      
      default:
        T_PutCharacter(tb, (unsigned char)*s);
    }
  }
}

void T_ClearScreen(TerminalBackend *tb)
{
  tb -> func_clear_screen(tb);
}

void T_PutCharacter(TerminalBackend *tb, uint32_t ch)
{
  tb -> func_put_character(tb, ch);
}

void T_GetCursorPosition(TerminalBackend *tb, uint16_t *x, uint16_t *y)
{
  tb -> func_get_cursor_position(tb, x, y);
}

void T_GetSize(TerminalBackend *tb, uint16_t *w, uint16_t *h)
{
  tb -> func_get_size(tb, w, h);
}

