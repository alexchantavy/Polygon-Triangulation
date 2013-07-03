/* Copyright (c) <2010>, <Alexander Chantavy>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <University of Hawaii at Manoa> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <Alexander Chantavy> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <Alexander Chantavy> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package alexchantavy;
 
/**
 * Translates an entered URL address that may contain special characters to ASCII encoding.
 * @author Jon Lai
 */
public class VerifyString{

/**
 * Performs the URL validation and sanitization.
 * @param address String of the URL address
 * @return The sanitized string.
 */
public static String verifyStrings(String address){
		String result = ""; //value of address with translated html encoding
		char[] addressChars = new char[address.length()+1]; 
		address.getChars(0, address.length(), addressChars, 0); 
		//breaks down String to array of chars, address string to characters of address in char array addressChars
		for (int i = 0; i <= address.length(); i++){
			//Non number and alphabets in ascii character set
			/*
			if (addressChars[i] == ' '){
				result = result.concat("%20");
			}
			else if (addressChars[i] == '!'){
				result = result.concat("%21");
			}
			else if (addressChars[i] == '"'){
				result = result.concat("%22");
			}
			else if (addressChars[i] == '#'){
				result = result.concat("%23");
			}
			else if (addressChars[i] == '$'){
				result = result.concat("%24");
			}
			else if (addressChars[i] == '%'){
				result = result.concat("%25");
			}
			else if (addressChars[i] == '&'){
				result = result.concat("%26");
			}
			else if (addressChars[i] == '\''){
				result = result.concat("%27");
			}
			else if (addressChars[i] == '('){
				result = result.concat("%28");
			}
			else if (addressChars[i] == ')'){
				result = result.concat("%29");
			}
			else if (addressChars[i] == '*'){
				result = result.concat("%2A");
			}
			else if (addressChars[i] == '+'){
				result = result.concat("%2B");
			}
			else if (addressChars[i] == ','){
				result = result.concat("%2C");
			}
			else if (addressChars[i] == '-'){
				result = result.concat("%2D");
			}
			else if (addressChars[i] == '.'){
				result = result.concat("%2E");
			}
			else if (addressChars[i] == '/'){
				result = result.concat("%2F");
			}
			else if (addressChars[i] == ':'){
				result = result.concat("%3A");
			}
			else if (addressChars[i] == ';'){
				result = result.concat("%3B");
			}
			else if (addressChars[i] == '<'){
				result = result.concat("%3C");
			}
			else if (addressChars[i] == '='){
				result = result.concat("%3D");
			}
			else if (addressChars[i] == '>'){
				result = result.concat("%3E");
			}
			else if (addressChars[i] == '?'){
				result = result.concat("%3F");
			}
			else if (addressChars[i] == '@'){
				result = result.concat("%40");
			}
			else if (addressChars[i] == '['){
				result = result.concat("%5B");
			}
			else if (addressChars[i] == '\\'){
				result = result.concat("%5C");
			}
			else if (addressChars[i] == ']'){
				result = result.concat("%5D");
			}
			else if (addressChars[i] == '^'){
				result = result.concat("%5E");
			}
			else if (addressChars[i] == '_'){
				result = result.concat("%5F");
			}
			else if (addressChars[i] == '`'){
				result = result.concat("%60");
			}
			else if (addressChars[i] == '{'){
				result = result.concat("%7B");
			}
			else if (addressChars[i] == '|'){
				result = result.concat("%7C");
			}
			else if (addressChars[i] == '}'){
				result = result.concat("%7D");
			}
			else if (addressChars[i] == '~'){
				result = result.concat("%7E");
			}
			*/
			
			//not in ascii character set
			if (addressChars[i] == ' '){
				result = result.concat("%7F");
			}
			else if (addressChars[i] == '€'){
				result = result.concat("%80");
			}
			else if (addressChars[i] == ' '){
				result = result.concat("%81");
			}
			else if (addressChars[i] == '‚'){
				result = result.concat("%82");
			}
			else if (addressChars[i] == 'ƒ'){
				result = result.concat("%83");
			}
			else if (addressChars[i] == '„'){
				result = result.concat("%84");
			}
			else if (addressChars[i] == '…'){
				result = result.concat("%85");
			}
			else if (addressChars[i] == '†'){
				result = result.concat("%86");
			}
			else if (addressChars[i] == '‡'){
				result = result.concat("%87");
			}
			else if (addressChars[i] == 'ˆ'){
				result = result.concat("%88");
			}
			else if (addressChars[i] == '‰'){
				result = result.concat("%89");
			}
			else if (addressChars[i] == 'Š'){
				result = result.concat("%8A");
			}
			else if (addressChars[i] == '‹'){
				result = result.concat("%8B");
			}
			else if (addressChars[i] == 'Œ'){
				result = result.concat("%8C");
			}
			else if (addressChars[i] == ' '){
				result = result.concat("%8D");
			}
			else if (addressChars[i] == 'Ž'){
				result = result.concat("%8E");
			}
			else if (addressChars[i] == ' '){
				result = result.concat("%8F");
			}
			else if (addressChars[i] == ' '){
				result = result.concat("%90");
			}
			else if (addressChars[i] == '‘'){
				result = result.concat("%91");
			}
			else if (addressChars[i] == '’'){
				result = result.concat("%92");
			}
			else if (addressChars[i] == '“'){
				result = result.concat("%93");
			}
			else if (addressChars[i] == '”'){
				result = result.concat("%94");
			}
			else if (addressChars[i] == '•'){
				result = result.concat("%95");
			}
			else if (addressChars[i] == '–'){
				result = result.concat("%96");
			}
			else if (addressChars[i] == '—'){
				result = result.concat("%97");
			}
			else if (addressChars[i] == '˜'){
				result = result.concat("%98");
			}
			else if (addressChars[i] == '™'){
				result = result.concat("%99");
			}
			else if (addressChars[i] == 'š'){
				result = result.concat("%9A");
			}
			else if (addressChars[i] == '›'){
				result = result.concat("%9B");
			}
			else if (addressChars[i] == 'œ'){
				result = result.concat("%9C");
			}
			else if (addressChars[i] == ' '){
				result = result.concat("%9D");
			}
			else if (addressChars[i] == 'ž'){
				result = result.concat("%9E");
			}
			else if (addressChars[i] == 'Ÿ'){
				result = result.concat("%9F");
			}
			else if (addressChars[i] == ' '){
				result = result.concat("%A0");
			}
			else if (addressChars[i] == '¡'){
				result = result.concat("%A1");
			}
			else if (addressChars[i] == '¢'){
				result = result.concat("%A2");
			}
			else if (addressChars[i] == '£'){
				result = result.concat("%A3");
			}
			else if (addressChars[i] == ' '){
				result = result.concat("%A4");
			}
			else if (addressChars[i] == '¥'){
				result = result.concat("%A5");
			}
			else if (addressChars[i] == '|'){
				result = result.concat("%A6");
			}
			else if (addressChars[i] == '§'){
				result = result.concat("%A7");
			}
			else if (addressChars[i] == '¨'){
				result = result.concat("%A8");
			}
			else if (addressChars[i] == '©'){
				result = result.concat("%A9");
			}
			else if (addressChars[i] == 'ª'){
				result = result.concat("%AA");
			}
			else if (addressChars[i] == '«'){
				result = result.concat("%AB");
			}
			else if (addressChars[i] == '¬'){
				result = result.concat("%AC");
			}
			else if (addressChars[i] == '¯'){
				result = result.concat("%AD");
			}
			else if (addressChars[i] == '®'){
				result = result.concat("%AE");
			}
			else if (addressChars[i] == '¯'){
				result = result.concat("%AF");
			}
			else if (addressChars[i] == '°'){
				result = result.concat("%B0");
			}
			else if (addressChars[i] == '±'){
				result = result.concat("%B1");
			}
			else if (addressChars[i] == '²'){
				result = result.concat("%B2");
			}
			else if (addressChars[i] == '³'){
				result = result.concat("%B3");
			}
			else if (addressChars[i] == '´'){
				result = result.concat("%B4");
			}
			else if (addressChars[i] == 'µ'){
				result = result.concat("%B5");
			}
			else if (addressChars[i] == '¶'){
				result = result.concat("%B6");
			}
			else if (addressChars[i] == '·'){
				result = result.concat("%B7");
			}
			else if (addressChars[i] == '¸'){
				result = result.concat("%B8");
			}
			else if (addressChars[i] == '¹'){
				result = result.concat("%B9");
			}
			else if (addressChars[i] == 'º'){
				result = result.concat("%BA");
			}
			else if (addressChars[i] == '»'){
				result = result.concat("%BB");
			}
			else if (addressChars[i] == '¼'){
				result = result.concat("%BC");
			}
			else if (addressChars[i] == '½'){
				result = result.concat("%BD");
			}
			else if (addressChars[i] == '¾'){
				result = result.concat("%BE");
			}
			else if (addressChars[i] == '¿'){
				result = result.concat("%BF");
			}
			else if (addressChars[i] == 'À'){
				result = result.concat("%C0");
			}
			else if (addressChars[i] == 'Á'){
				result = result.concat("%C1");
			}
			else if (addressChars[i] == 'Â'){
				result = result.concat("%C2");
			}
			else if (addressChars[i] == 'Ã'){
				result = result.concat("%C3");
			}
			else if (addressChars[i] == 'Ä'){
				result = result.concat("%C4");
			}
			else if (addressChars[i] == 'Å'){
				result = result.concat("%C5");
			}
			else if (addressChars[i] == 'Æ'){
				result = result.concat("%C6");
			}
			else if (addressChars[i] == 'Ç'){
				result = result.concat("%C7");
			}
			else if (addressChars[i] == 'È'){
				result = result.concat("%C8");
			}
			else if (addressChars[i] == 'É'){
				result = result.concat("%C9");
			}
			else if (addressChars[i] == 'Ê'){
				result = result.concat("%CA");
			}
			else if (addressChars[i] == 'Ë'){
				result = result.concat("%CB");
			}
			else if (addressChars[i] == 'Ì'){
				result = result.concat("%CC");
			}
			else if (addressChars[i] == 'Í'){
				result = result.concat("%CD");
			}
			else if (addressChars[i] == 'Î'){
				result = result.concat("%CE");
			}
			else if (addressChars[i] == 'Ï'){
				result = result.concat("%CF");
			}
			else if (addressChars[i] == 'Ð'){
				result = result.concat("%D0");
			}
			else if (addressChars[i] == 'Ñ'){
				result = result.concat("%D1");
			}
			else if (addressChars[i] == 'Ò'){
				result = result.concat("%D2");
			}
			else if (addressChars[i] == 'Ó'){
				result = result.concat("%D3");
			}
			else if (addressChars[i] == 'Ô'){
				result = result.concat("%D4");
			}
			else if (addressChars[i] == 'Õ'){
				result = result.concat("%D5");
			}
			else if (addressChars[i] == 'Ö'){
				result = result.concat("%D6");
			}
			else if (addressChars[i] == ' '){
				result = result.concat("%D7");
			}
			else if (addressChars[i] == 'Ø'){
				result = result.concat("%D8");
			}
			else if (addressChars[i] == 'Ù'){
				result = result.concat("%D9");
			}
			else if (addressChars[i] == 'Ú'){
				result = result.concat("%DA");
			}
			else if (addressChars[i] == 'Û'){
				result = result.concat("%DB");
			}
			else if (addressChars[i] == 'Ü'){
				result = result.concat("%DC");
			}
			else if (addressChars[i] == 'Ý'){
				result = result.concat("%DD");
			}
			else if (addressChars[i] == 'Þ'){
				result = result.concat("%DE");
			}
			else if (addressChars[i] == 'ß'){
				result = result.concat("%DF");
			}
			else if (addressChars[i] == 'à'){
				result = result.concat("%E0");
			}
			else if (addressChars[i] == 'á'){
				result = result.concat("%E1");
			}
			else if (addressChars[i] == 'â'){
				result = result.concat("%E2");
			}
			else if (addressChars[i] == 'ã'){
				result = result.concat("%E3");
			}
			else if (addressChars[i] == 'ä'){
				result = result.concat("%E4");
			}
			else if (addressChars[i] == 'å'){
				result = result.concat("%E5");
			}
			else if (addressChars[i] == 'æ'){
				result = result.concat("%E6");
			}
			else if (addressChars[i] == 'ç'){
				result = result.concat("%E7");
			}
			else if (addressChars[i] == 'è'){
				result = result.concat("%E8");
			}
			else if (addressChars[i] == 'é'){
				result = result.concat("%E9");
			}
			else if (addressChars[i] == 'ê'){
				result = result.concat("%EA");
			}
			else if (addressChars[i] == 'ë'){
				result = result.concat("%EB");
			}
			else if (addressChars[i] == 'ì'){
				result = result.concat("%EC");
			}
			else if (addressChars[i] == 'í'){
				result = result.concat("%ED");
			}
			else if (addressChars[i] == 'î'){
				result = result.concat("%EE");
			}
			else if (addressChars[i] == 'ï'){
				result = result.concat("%EF");
			}
			else if (addressChars[i] == 'ð'){
				result = result.concat("%F0");
			}
			else if (addressChars[i] == 'ñ'){
				result = result.concat("%F1");
			}
			else if (addressChars[i] == 'ò'){
				result = result.concat("%F2");
			}
			else if (addressChars[i] == 'ó'){
				result = result.concat("%F3");
			}
			else if (addressChars[i] == 'ô'){
				result = result.concat("%F4");
			}
			else if (addressChars[i] == 'õ'){
				result = result.concat("%F5");
			}
			else if (addressChars[i] == 'ö'){
				result = result.concat("%F6");
			}
			else if (addressChars[i] == '÷'){
				result = result.concat("%F7");
			}
			else if (addressChars[i] == 'ø'){
				result = result.concat("%F8");
			}
			else if (addressChars[i] == 'ù'){
				result = result.concat("%F9");
			}
			else if (addressChars[i] == 'ú'){
				result = result.concat("%FA");
			}
			else if (addressChars[i] == 'û'){
				result = result.concat("%FB");
			}
			else if (addressChars[i] == 'ü'){
				result = result.concat("%FC");
			}
			else if (addressChars[i] == 'ý'){
				result = result.concat("%FD");
			}
			else if (addressChars[i] == 'þ'){
				result = result.concat("%FE");
			}
			else if (addressChars[i] == 'ÿ'){
				result = result.concat("%FF");
			}
			else result = result.concat(Character.toString(addressChars[i]));
		}
		return result;
	}
}