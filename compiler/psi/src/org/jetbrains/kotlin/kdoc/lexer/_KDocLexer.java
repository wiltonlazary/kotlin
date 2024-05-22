// Generated by JFlex 1.9.2 http://jflex.de/  (tweaked for IntelliJ platform)
// source: src/org/jetbrains/kotlin/kdoc/lexer/KDoc.flex

package org.jetbrains.kotlin.kdoc.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharArrayUtil;
import java.lang.Character;
import org.jetbrains.kotlin.kdoc.parser.KDocKnownTag;


class _KDocLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int LINE_BEGINNING = 2;
  public static final int CONTENTS_BEGINNING = 4;
  public static final int TAG_BEGINNING = 6;
  public static final int TAG_TEXT_BEGINNING = 8;
  public static final int CONTENTS = 10;
  public static final int CODE_BLOCK = 12;
  public static final int CODE_BLOCK_LINE_BEGINNING = 14;
  public static final int CODE_BLOCK_CONTENTS_BEGINNING = 16;
  public static final int INDENTED_CODE_BLOCK = 18;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = {
     0,  0,  1,  1,  2,  2,  3,  3,  4,  4,  5,  5,  6,  6,  7,  7, 
     8,  8,  6, 6
  };

  /**
   * Top-level table for translating characters to character classes
   */
  private static final int [] ZZ_CMAP_TOP = zzUnpackcmap_top();

  private static final String ZZ_CMAP_TOP_PACKED_0 =
    "\1\0\1\u0100\1\u0200\1\u0300\1\u0400\1\u0500\1\u0600\1\u0700"+
    "\1\u0800\1\u0900\1\u0a00\1\u0b00\1\u0c00\1\u0d00\1\u0e00\1\u0f00"+
    "\1\u1000\1\u0100\1\u1100\1\u1200\1\u1300\1\u0100\1\u1400\1\u1500"+
    "\1\u1600\1\u1700\1\u1800\1\u1900\1\u1a00\1\u1b00\1\u0100\1\u1c00"+
    "\1\u1d00\1\u1e00\12\u1f00\1\u2000\1\u2100\1\u2200\1\u1f00\1\u2300"+
    "\1\u2400\2\u1f00\31\u0100\1\u1b00\121\u0100\1\u2500\4\u0100\1\u2600"+
    "\1\u0100\1\u2700\1\u2800\1\u2900\1\u2a00\1\u2b00\1\u2c00\53\u0100"+
    "\1\u2d00\10\u2e00\31\u1f00\1\u0100\1\u2f00\1\u3000\1\u0100\1\u3100"+
    "\1\u3200\1\u3300\1\u3400\1\u3500\1\u3600\1\u3700\1\u3800\1\u3900"+
    "\1\u0100\1\u3a00\1\u3b00\1\u3c00\1\u3d00\1\u3e00\1\u3f00\1\u4000"+
    "\1\u4100\1\u4200\1\u4300\1\u4400\1\u4500\1\u4600\1\u4700\1\u4800"+
    "\1\u4900\1\u4a00\1\u4b00\1\u4c00\1\u4d00\1\u1f00\1\u4e00\1\u4f00"+
    "\1\u5000\1\u5100\3\u0100\1\u5200\1\u5300\1\u5400\12\u1f00\4\u0100"+
    "\1\u5500\17\u1f00\2\u0100\1\u5600\41\u1f00\2\u0100\1\u5700\1\u5800"+
    "\2\u1f00\1\u5900\1\u5a00\27\u0100\1\u5b00\4\u0100\1\u5c00\1\u5d00"+
    "\42\u1f00\1\u0100\1\u5e00\1\u5f00\11\u1f00\1\u6000\27\u1f00\1\u6100"+
    "\1\u6200\1\u6300\1\u6400\11\u1f00\1\u6500\1\u6600\5\u1f00\1\u6700"+
    "\1\u6800\2\u1f00\1\u6900\1\u1f00\1\u6a00\21\u1f00\246\u0100\1\u6b00"+
    "\20\u0100\1\u6c00\1\u6d00\25\u0100\1\u6e00\34\u0100\1\u6f00\14\u1f00"+
    "\2\u0100\1\u7000\5\u1f00\23\u0100\1\u7100\u0dec\u1f00";

  private static int [] zzUnpackcmap_top() {
    int [] result = new int[4352];
    int offset = 0;
    offset = zzUnpackcmap_top(ZZ_CMAP_TOP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackcmap_top(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Second-level tables for translating characters to character classes
   */
  private static final int [] ZZ_CMAP_BLOCKS = zzUnpackcmap_blocks();

  private static final String ZZ_CMAP_BLOCKS_PACKED_0 =
    "\11\0\1\1\1\2\1\3\1\4\1\3\22\0\1\5"+
    "\3\0\1\6\3\0\1\7\1\10\1\11\3\0\1\12"+
    "\1\13\12\14\6\0\1\15\32\6\1\16\1\17\1\20"+
    "\1\0\1\6\1\21\32\6\3\0\1\22\6\0\1\3"+
    "\34\0\4\6\4\0\1\6\12\0\1\6\4\0\1\6"+
    "\5\0\27\6\1\0\37\6\1\0\u01ca\6\4\0\14\6"+
    "\16\0\5\6\7\0\1\6\1\0\1\6\201\0\5\6"+
    "\1\0\2\6\2\0\4\6\1\0\1\6\6\0\1\6"+
    "\1\0\3\6\1\0\1\6\1\0\24\6\1\0\123\6"+
    "\1\0\213\6\10\0\246\6\1\0\46\6\2\0\1\6"+
    "\6\0\51\6\6\0\1\6\100\0\33\6\4\0\4\6"+
    "\30\0\1\6\24\0\53\6\43\0\2\6\1\0\143\6"+
    "\1\0\1\6\17\0\2\6\7\0\2\6\12\0\3\6"+
    "\2\0\1\6\20\0\1\6\1\0\36\6\35\0\131\6"+
    "\13\0\1\6\30\0\41\6\11\0\2\6\4\0\1\6"+
    "\3\0\30\6\4\0\1\6\11\0\1\6\3\0\1\6"+
    "\27\0\31\6\7\0\13\6\65\0\25\6\1\0\22\6"+
    "\74\0\66\6\3\0\1\6\22\0\1\6\7\0\12\6"+
    "\17\0\20\6\4\0\10\6\2\0\2\6\2\0\26\6"+
    "\1\0\7\6\1\0\1\6\3\0\4\6\3\0\1\6"+
    "\20\0\1\6\15\0\2\6\1\0\3\6\16\0\4\6"+
    "\7\0\2\6\10\0\6\6\4\0\2\6\2\0\26\6"+
    "\1\0\7\6\1\0\2\6\1\0\2\6\1\0\2\6"+
    "\37\0\4\6\1\0\1\6\23\0\3\6\20\0\11\6"+
    "\1\0\3\6\1\0\26\6\1\0\7\6\1\0\2\6"+
    "\1\0\5\6\3\0\1\6\22\0\1\6\17\0\2\6"+
    "\17\0\1\6\7\0\1\6\13\0\10\6\2\0\2\6"+
    "\2\0\26\6\1\0\7\6\1\0\2\6\1\0\5\6"+
    "\3\0\1\6\36\0\2\6\1\0\3\6\17\0\1\6"+
    "\21\0\1\6\1\0\6\6\3\0\3\6\1\0\4\6"+
    "\3\0\2\6\1\0\1\6\1\0\2\6\3\0\2\6"+
    "\3\0\3\6\3\0\14\6\26\0\1\6\50\0\1\6"+
    "\13\0\10\6\1\0\3\6\1\0\27\6\1\0\20\6"+
    "\3\0\1\6\32\0\3\6\5\0\2\6\36\0\1\6"+
    "\4\0\10\6\1\0\3\6\1\0\27\6\1\0\12\6"+
    "\1\0\5\6\3\0\1\6\40\0\1\6\1\0\2\6"+
    "\17\0\2\6\21\0\11\6\1\0\3\6\1\0\51\6"+
    "\2\0\1\6\20\0\1\6\5\0\3\6\10\0\3\6"+
    "\30\0\6\6\5\0\22\6\3\0\30\6\1\0\11\6"+
    "\1\0\1\6\2\0\7\6\72\0\60\6\1\0\2\6"+
    "\13\0\10\6\72\0\2\6\1\0\1\6\1\0\5\6"+
    "\1\0\30\6\1\0\1\6\1\0\12\6\1\0\2\6"+
    "\11\0\1\6\2\0\5\6\1\0\1\6\25\0\4\6"+
    "\40\0\1\6\77\0\10\6\1\0\44\6\33\0\5\6"+
    "\163\0\53\6\24\0\1\6\20\0\6\6\4\0\4\6"+
    "\3\0\1\6\3\0\2\6\7\0\3\6\4\0\15\6"+
    "\14\0\1\6\21\0\46\6\1\0\1\6\5\0\1\6"+
    "\2\0\53\6\1\0\115\6\1\0\4\6\2\0\7\6"+
    "\1\0\1\6\1\0\4\6\2\0\51\6\1\0\4\6"+
    "\2\0\41\6\1\0\4\6\2\0\7\6\1\0\1\6"+
    "\1\0\4\6\2\0\17\6\1\0\71\6\1\0\4\6"+
    "\2\0\103\6\45\0\20\6\20\0\126\6\2\0\6\6"+
    "\3\0\u016c\6\2\0\21\6\1\0\32\6\5\0\113\6"+
    "\3\0\13\6\7\0\15\6\1\0\4\6\16\0\22\6"+
    "\16\0\22\6\16\0\15\6\1\0\3\6\17\0\64\6"+
    "\43\0\1\6\3\0\2\6\103\0\131\6\7\0\5\6"+
    "\2\0\42\6\1\0\1\6\5\0\106\6\12\0\37\6"+
    "\61\0\36\6\2\0\5\6\13\0\54\6\4\0\32\6"+
    "\66\0\27\6\11\0\65\6\122\0\1\6\135\0\57\6"+
    "\21\0\7\6\67\0\36\6\15\0\2\6\12\0\54\6"+
    "\32\0\44\6\51\0\3\6\12\0\44\6\2\0\11\6"+
    "\7\0\53\6\2\0\3\6\51\0\4\6\1\0\6\6"+
    "\1\0\2\6\3\0\1\6\5\0\300\6\100\0\26\6"+
    "\2\0\6\6\2\0\46\6\2\0\6\6\2\0\10\6"+
    "\1\0\1\6\1\0\1\6\1\0\1\6\1\0\37\6"+
    "\2\0\65\6\1\0\7\6\1\0\1\6\3\0\3\6"+
    "\1\0\7\6\3\0\4\6\2\0\6\6\4\0\15\6"+
    "\5\0\3\6\1\0\7\6\53\0\2\3\25\0\2\6"+
    "\23\0\1\6\34\0\1\6\15\0\1\6\20\0\15\6"+
    "\3\0\40\6\102\0\1\6\4\0\1\6\2\0\12\6"+
    "\1\0\1\6\3\0\5\6\6\0\1\6\1\0\1\6"+
    "\1\0\1\6\1\0\4\6\1\0\13\6\2\0\4\6"+
    "\5\0\5\6\4\0\1\6\21\0\51\6\u0177\0\57\6"+
    "\1\0\57\6\1\0\205\6\6\0\4\6\3\0\2\6"+
    "\14\0\46\6\1\0\1\6\5\0\1\6\2\0\70\6"+
    "\7\0\1\6\20\0\27\6\11\0\7\6\1\0\7\6"+
    "\1\0\7\6\1\0\7\6\1\0\7\6\1\0\7\6"+
    "\1\0\7\6\1\0\7\6\120\0\1\6\325\0\3\6"+
    "\31\0\11\6\7\0\5\6\2\0\5\6\4\0\126\6"+
    "\6\0\3\6\1\0\132\6\1\0\4\6\5\0\53\6"+
    "\1\0\136\6\21\0\40\6\60\0\u010d\6\3\0\215\6"+
    "\103\0\56\6\2\0\15\6\3\0\20\6\12\0\2\6"+
    "\24\0\57\6\20\0\37\6\2\0\120\6\47\0\11\6"+
    "\2\0\147\6\2\0\65\6\2\0\11\6\52\0\15\6"+
    "\1\0\3\6\1\0\4\6\1\0\27\6\25\0\1\6"+
    "\7\0\64\6\16\0\62\6\76\0\6\6\3\0\1\6"+
    "\1\0\2\6\13\0\34\6\12\0\27\6\31\0\35\6"+
    "\7\0\57\6\34\0\1\6\20\0\5\6\1\0\12\6"+
    "\12\0\5\6\1\0\51\6\27\0\3\6\1\0\10\6"+
    "\24\0\27\6\3\0\1\6\3\0\62\6\1\0\1\6"+
    "\3\0\2\6\2\0\5\6\2\0\1\6\1\0\1\6"+
    "\30\0\3\6\2\0\13\6\7\0\3\6\14\0\6\6"+
    "\2\0\6\6\2\0\6\6\11\0\7\6\1\0\7\6"+
    "\1\0\53\6\1\0\16\6\6\0\163\6\35\0\244\6"+
    "\14\0\27\6\4\0\61\6\4\0\u0100\3\156\6\2\0"+
    "\152\6\46\0\7\6\14\0\5\6\5\0\1\6\1\0"+
    "\12\6\1\0\15\6\1\0\5\6\1\0\1\6\1\0"+
    "\2\6\1\0\2\6\1\0\154\6\41\0\153\6\22\0"+
    "\100\6\2\0\66\6\50\0\15\6\66\0\2\6\30\0"+
    "\3\6\31\0\1\6\6\0\5\6\1\0\207\6\7\0"+
    "\1\6\34\0\32\6\4\0\1\6\1\0\32\6\13\0"+
    "\131\6\3\0\6\6\2\0\6\6\2\0\6\6\2\0"+
    "\3\6\3\0\2\6\3\0\2\6\31\0\14\6\1\0"+
    "\32\6\1\0\23\6\1\0\2\6\1\0\17\6\2\0"+
    "\16\6\42\0\173\6\105\0\65\6\u010b\0\35\6\3\0"+
    "\61\6\57\0\40\6\15\0\36\6\5\0\46\6\12\0"+
    "\36\6\2\0\44\6\4\0\10\6\1\0\5\6\52\0"+
    "\236\6\22\0\44\6\4\0\44\6\4\0\50\6\10\0"+
    "\64\6\234\0\67\6\11\0\26\6\12\0\10\6\230\0"+
    "\6\6\2\0\1\6\1\0\54\6\1\0\2\6\3\0"+
    "\1\6\2\0\27\6\12\0\27\6\11\0\37\6\101\0"+
    "\23\6\1\0\2\6\12\0\26\6\12\0\32\6\106\0"+
    "\70\6\6\0\2\6\100\0\1\6\17\0\4\6\1\0"+
    "\3\6\1\0\35\6\52\0\35\6\3\0\35\6\43\0"+
    "\10\6\1\0\34\6\33\0\66\6\12\0\26\6\12\0"+
    "\23\6\15\0\22\6\156\0\111\6\67\0\63\6\15\0"+
    "\63\6\15\0\44\6\u015c\0\52\6\6\0\2\6\116\0"+
    "\35\6\12\0\1\6\10\0\26\6\152\0\25\6\33\0"+
    "\27\6\14\0\65\6\113\0\55\6\40\0\31\6\32\0"+
    "\44\6\35\0\1\6\2\0\1\6\10\0\43\6\3\0"+
    "\1\6\14\0\60\6\16\0\4\6\25\0\1\6\1\0"+
    "\1\6\43\0\22\6\1\0\31\6\124\0\7\6\1\0"+
    "\1\6\1\0\4\6\1\0\17\6\1\0\12\6\7\0"+
    "\57\6\46\0\10\6\2\0\2\6\2\0\26\6\1\0"+
    "\7\6\1\0\2\6\1\0\5\6\3\0\1\6\22\0"+
    "\1\6\14\0\5\6\236\0\65\6\22\0\4\6\24\0"+
    "\3\6\36\0\60\6\24\0\2\6\1\0\1\6\270\0"+
    "\57\6\51\0\4\6\44\0\60\6\24\0\1\6\73\0"+
    "\53\6\15\0\1\6\107\0\33\6\345\0\54\6\164\0"+
    "\100\6\37\0\10\6\2\0\1\6\2\0\10\6\1\0"+
    "\2\6\1\0\30\6\17\0\1\6\1\0\1\6\136\0"+
    "\10\6\2\0\47\6\20\0\1\6\1\0\1\6\34\0"+
    "\1\6\12\0\50\6\7\0\1\6\25\0\1\6\13\0"+
    "\56\6\23\0\1\6\42\0\71\6\7\0\11\6\1\0"+
    "\45\6\21\0\1\6\61\0\36\6\160\0\7\6\1\0"+
    "\2\6\1\0\46\6\25\0\1\6\31\0\6\6\1\0"+
    "\2\6\1\0\40\6\16\0\1\6\u0147\0\23\6\275\0"+
    "\1\6\54\0\4\6\37\0\232\6\146\0\157\6\21\0"+
    "\304\6\274\0\57\6\321\0\107\6\271\0\71\6\7\0"+
    "\37\6\161\0\36\6\22\0\60\6\20\0\4\6\37\0"+
    "\25\6\5\0\23\6\260\0\100\6\200\0\113\6\5\0"+
    "\1\6\102\0\15\6\100\0\2\6\1\0\1\6\34\0"+
    "\370\6\10\0\326\6\52\0\11\6\367\0\37\6\61\0"+
    "\3\6\21\0\4\6\10\0\u018c\6\4\0\153\6\5\0"+
    "\15\6\3\0\11\6\7\0\12\6\146\0\125\6\1\0"+
    "\107\6\1\0\2\6\2\0\1\6\2\0\2\6\2\0"+
    "\4\6\1\0\14\6\1\0\1\6\1\0\7\6\1\0"+
    "\101\6\1\0\4\6\2\0\10\6\1\0\7\6\1\0"+
    "\34\6\1\0\4\6\1\0\5\6\1\0\1\6\3\0"+
    "\7\6\1\0\u0154\6\2\0\31\6\1\0\31\6\1\0"+
    "\37\6\1\0\31\6\1\0\37\6\1\0\31\6\1\0"+
    "\37\6\1\0\31\6\1\0\37\6\1\0\31\6\1\0"+
    "\10\6\64\0\55\6\12\0\7\6\20\0\1\6\u0171\0"+
    "\54\6\23\0\306\6\73\0\104\6\7\0\1\6\u0164\0"+
    "\1\6\117\0\4\6\1\0\33\6\1\0\2\6\1\0"+
    "\1\6\2\0\1\6\1\0\12\6\1\0\4\6\1\0"+
    "\1\6\1\0\1\6\6\0\1\6\4\0\1\6\1\0"+
    "\1\6\1\0\1\6\1\0\3\6\1\0\2\6\1\0"+
    "\1\6\2\0\1\6\1\0\1\6\1\0\1\6\1\0"+
    "\1\6\1\0\1\6\1\0\2\6\1\0\1\6\2\0"+
    "\4\6\1\0\7\6\1\0\4\6\1\0\4\6\1\0"+
    "\1\6\1\0\12\6\1\0\21\6\5\0\3\6\1\0"+
    "\5\6\1\0\21\6\104\0\336\6\42\0\65\6\13\0"+
    "\336\6\2\0\u0182\6\16\0\u0131\6\37\0\36\6\342\0"+
    "\113\6\265\0";

  private static int [] zzUnpackcmap_blocks() {
    int [] result = new int[29184];
    int offset = 0;
    offset = zzUnpackcmap_blocks(ZZ_CMAP_BLOCKS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackcmap_blocks(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /**
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\11\0\3\1\1\2\1\3\2\4\1\5\6\2\1\6"+
    "\1\7\2\2\1\10\1\11\1\10\1\12\2\10\1\0"+
    "\1\13\1\0\1\4\3\0\1\14\2\0\1\15\4\0"+
    "\1\16\1\4\2\0\1\17\1\20\1\21\1\0\1\3"+
    "\2\22\1\23\1\0\1\24\1\23";

  private static int [] zzUnpackAction() {
    int [] result = new int[63];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\23\0\46\0\71\0\114\0\137\0\162\0\205"+
    "\0\230\0\253\0\276\0\321\0\253\0\344\0\367\0\u010a"+
    "\0\u011d\0\u0130\0\u0143\0\u0156\0\u0169\0\276\0\u017c\0\u018f"+
    "\0\u01a2\0\u01b5\0\u01c8\0\253\0\u01db\0\276\0\u01ee\0\u0201"+
    "\0\u0214\0\276\0\253\0\u0227\0\u023a\0\u024d\0\u0260\0\u0273"+
    "\0\253\0\u0286\0\u0299\0\u02ac\0\u02bf\0\u02d2\0\u02e5\0\u02f8"+
    "\0\253\0\u030b\0\u031e\0\u0331\0\u0344\0\253\0\253\0\u0357"+
    "\0\u030b\0\u024d\0\u0273\0\u024d\0\u036a\0\u037d\0\253";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[63];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length() - 1;
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /**
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpacktrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\11\12\1\13\1\12\1\14\7\12\1\15\1\16\1\17"+
    "\1\15\1\17\1\20\3\15\1\21\4\15\1\22\1\23"+
    "\1\15\1\24\1\25\1\15\1\16\1\17\1\15\1\17"+
    "\1\20\3\15\1\26\3\15\1\27\1\22\1\23\1\15"+
    "\1\24\1\25\1\15\2\30\1\15\2\30\1\31\2\15"+
    "\1\26\4\15\1\32\5\15\2\30\1\15\2\30\3\15"+
    "\1\26\4\15\1\33\5\15\1\16\1\17\1\15\1\17"+
    "\1\20\3\15\1\26\4\15\1\22\1\23\1\15\1\24"+
    "\1\25\1\34\2\35\1\34\2\35\3\34\1\36\12\34"+
    "\2\35\1\34\2\35\3\34\1\37\7\34\1\40\1\41"+
    "\1\34\2\35\1\34\2\35\3\34\1\36\7\34\1\40"+
    "\1\41\34\0\1\42\1\0\1\43\20\0\1\44\12\0"+
    "\1\16\1\17\1\0\2\17\16\0\2\17\1\0\2\17"+
    "\16\0\2\17\1\0\1\17\1\45\26\0\1\21\1\0"+
    "\1\43\7\0\6\46\1\47\7\46\1\0\1\46\1\50"+
    "\2\46\16\0\1\51\1\0\1\51\23\0\1\52\23\0"+
    "\1\53\6\0\1\54\15\0\2\30\1\0\2\30\23\0"+
    "\1\31\3\0\1\31\1\0\1\31\14\0\1\55\22\0"+
    "\1\56\15\0\2\35\1\0\2\35\26\0\1\37\1\0"+
    "\1\43\30\0\1\57\23\0\1\60\11\0\1\61\12\0"+
    "\2\17\1\0\1\17\1\62\15\0\16\46\1\0\1\46"+
    "\1\50\10\46\1\47\3\46\1\47\1\46\1\47\1\46"+
    "\1\0\1\46\1\63\11\46\1\64\6\46\1\0\1\46"+
    "\1\50\2\46\21\0\1\65\23\0\1\65\6\0\1\54"+
    "\5\0\1\54\14\0\1\55\3\0\1\55\1\0\1\55"+
    "\3\0\1\66\10\0\1\56\3\0\1\56\1\0\1\56"+
    "\3\0\1\67\23\0\1\70\23\0\1\70\1\0\2\17"+
    "\1\0\1\17\1\71\15\0\7\72\1\64\6\72\1\0"+
    "\1\72\1\73\2\72\10\64\1\74\5\64\1\75\4\64"+
    "\2\65\3\0\16\65\1\0\1\70\1\76\1\0\2\70"+
    "\15\0\10\75\1\77\12\75\1\0\1\76\2\0\2\76"+
    "\15\0";

  private static int [] zzUnpacktrans() {
    int [] result = new int[912];
    int offset = 0;
    offset = zzUnpacktrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpacktrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state {@code aState}
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\11\0\1\11\2\1\1\11\16\1\1\11\5\1\1\0"+
    "\1\11\1\0\1\1\3\0\1\11\2\0\1\1\4\0"+
    "\1\11\1\1\2\0\1\1\2\11\1\0\4\1\1\0"+
    "\1\1\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[63];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** Number of newlines encountered up to the start of the matched text. */
  @SuppressWarnings("unused")
  private int yyline;

  /** Number of characters from the last newline up to the start of the matched text. */
  @SuppressWarnings("unused")
  protected int yycolumn;

  /** Number of characters up to the start of the matched text. */
  @SuppressWarnings("unused")
  private long yychar;

  /** Whether the scanner is currently at the beginning of a line. */
  @SuppressWarnings("unused")
  private boolean zzAtBOL = true;

  /** Whether the user-EOF-code has already been executed. */
  private boolean zzEOFDone;

  /* user code: */
  public _KDocLexer() {
    this((java.io.Reader)null);
  }

  private boolean isLastToken() {
    return zzMarkedPos == zzBuffer.length();
  }

  private boolean yytextContainLineBreaks() {
    return CharArrayUtil.containLineBreaks(zzBuffer, zzStartRead, zzMarkedPos);
  }

  private boolean nextIsNotWhitespace() {
    return zzMarkedPos <= zzBuffer.length() && !Character.isWhitespace(zzBuffer.charAt(zzMarkedPos + 1));
  }

  private boolean prevIsNotWhitespace() {
    return zzMarkedPos != 0 && !Character.isWhitespace(zzBuffer.charAt(zzMarkedPos - 1));
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  _KDocLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** Returns the maximum size of the scanner buffer, which limits the size of tokens. */
  private int zzMaxBufferLen() {
    return Integer.MAX_VALUE;
  }

  /**  Whether the scanner buffer can grow to accommodate a larger token. */
  private boolean zzCanGrow() {
    return true;
  }

  /**
   * Translates raw input code points to DFA table row
   */
  private static int zzCMap(int input) {
    int offset = input & 255;
    return offset == input ? ZZ_CMAP_BLOCKS[offset] : ZZ_CMAP_BLOCKS[ZZ_CMAP_TOP[input >> 8] | offset];
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      {@code false}, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position {@code pos} from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
  return;
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException
  {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMap(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1:
            { return TokenType.BAD_CHARACTER;
            }
          // fall through
          case 21: break;
          case 2:
            { yybegin(CONTENTS);
        return KDocTokens.TEXT;
            }
          // fall through
          case 22: break;
          case 3:
            { if(yystate() == CONTENTS_BEGINNING) {
            yybegin(INDENTED_CODE_BLOCK);
            return KDocTokens.CODE_BLOCK_TEXT;
        }
            }
          // fall through
          case 23: break;
          case 4:
            { if (yytextContainLineBreaks()) {
            yybegin(LINE_BEGINNING);
            return TokenType.WHITE_SPACE;
        }  else {
            yybegin(yystate() == CONTENTS_BEGINNING ? CONTENTS_BEGINNING : CONTENTS);
            return KDocTokens.TEXT;  // internal white space
        }
            }
          // fall through
          case 24: break;
          case 5:
            { yybegin(CONTENTS_BEGINNING);
                                            return KDocTokens.LEADING_ASTERISK;
            }
          // fall through
          case 25: break;
          case 6:
            { if (yytextContainLineBreaks()) {
            yybegin(LINE_BEGINNING);
        }
        return TokenType.WHITE_SPACE;
            }
          // fall through
          case 26: break;
          case 7:
            { yybegin(TAG_TEXT_BEGINNING);
        return KDocTokens.MARKDOWN_LINK;
            }
          // fall through
          case 27: break;
          case 8:
            { yybegin(yystate() == INDENTED_CODE_BLOCK ? INDENTED_CODE_BLOCK : CODE_BLOCK);
        return KDocTokens.CODE_BLOCK_TEXT;
            }
          // fall through
          case 28: break;
          case 9:
            { if (yytextContainLineBreaks()) {
            yybegin(yystate() == INDENTED_CODE_BLOCK ? LINE_BEGINNING : CODE_BLOCK_LINE_BEGINNING);
            return TokenType.WHITE_SPACE;
        }
        return KDocTokens.CODE_BLOCK_TEXT;
            }
          // fall through
          case 29: break;
          case 10:
            { yybegin(CODE_BLOCK_CONTENTS_BEGINNING);
        return KDocTokens.LEADING_ASTERISK;
            }
          // fall through
          case 30: break;
          case 11:
            { if (isLastToken()) return KDocTokens.END;
                                            else return KDocTokens.TEXT;
            }
          // fall through
          case 31: break;
          case 12:
            { yybegin(CONTENTS);
        return KDocTokens.MARKDOWN_ESCAPED_CHAR;
            }
          // fall through
          case 32: break;
          case 13:
            { KDocKnownTag tag = KDocKnownTag.Companion.findByTagName(zzBuffer.subSequence(zzStartRead, zzMarkedPos));
    yybegin(tag != null && tag.isReferenceRequired() ? TAG_BEGINNING : TAG_TEXT_BEGINNING);
    return KDocTokens.TAG_NAME;
            }
          // fall through
          case 33: break;
          case 14:
            { yybegin(CONTENTS_BEGINNING);
                                            return KDocTokens.START;
            }
          // fall through
          case 34: break;
          case 15:
            { yybegin(CODE_BLOCK_LINE_BEGINNING);
        return KDocTokens.TEXT;
            }
          // fall through
          case 35: break;
          case 16:
            { yybegin(TAG_TEXT_BEGINNING);
                  return KDocTokens.MARKDOWN_LINK;
            }
          // fall through
          case 36: break;
          case 17:
            { yybegin(CONTENTS);
                  return KDocTokens.MARKDOWN_LINK;
            }
          // fall through
          case 37: break;
          case 18:
            // lookahead expression with fixed lookahead length
            zzMarkedPos = Character.offsetByCodePoints
                (zzBufferL, zzMarkedPos, -1);
            { yybegin(CONTENTS);
        return KDocTokens.MARKDOWN_LINK;
            }
          // fall through
          case 38: break;
          case 19:
            { yybegin(CONTENTS);
        return KDocTokens.MARKDOWN_INLINE_LINK;
            }
          // fall through
          case 39: break;
          case 20:
            // lookahead expression with fixed base length
            zzMarkedPos = Character.offsetByCodePoints
                (zzBufferL, zzStartRead, 3);
            { // Code fence end
        yybegin(CONTENTS);
        return KDocTokens.TEXT;
            }
          // fall through
          case 40: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
