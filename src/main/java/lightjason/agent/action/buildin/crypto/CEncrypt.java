/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.agent.action.buildin.crypto;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;


/**
 * encrypting algorithm
 *
 * @bug not working see http://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption/992413#992413 /
 * http://stackoverflow.com/questions/1205135/how-to-encrypt-string-in-java / http://stackoverflow.com/questions/3451670/java-aes-and-using-my-own-key
 */
public final class CEncrypt extends IBuildinAction
{

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 3;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        try
        {
            final EType l_encrypt = EType.valueOf( CCommon.<String, ITerm>getRawValue( p_argument.get( 0 ) ).trim().toUpperCase() );
            l_encrypt.getEncryptCipher( l_encrypt.generateKey( CCommon.getRawValue( p_argument.get( 1 ) ) ) );

            return CBoolean.from( true );
        }
        catch ( final NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException p_exception )
        {
            return CBoolean.from( false );
        }
    }


    /**
     * enum with encrypting types
     */
    private enum EType
    {
        AES( "AES/ECB/PKCS5Padding" ),
        DES( "DESede/ECB/PKCS5Padding" ),
        RSA( "RSA/ECB/OAEPWithSHA-256AndMGF1Padding" );

        /**
         * chipher name
         */
        private final String m_id;

        /**
         * ctor
         *
         * @param p_id chipher name
         */
        EType( final String p_id )
        {
            m_id = p_id;
        }

        /**
         * generates a key
         *
         * @param p_key string key
         * @return key object
         *
         * @throws UnsupportedEncodingException on encoding error
         */
        public final Key generateKey( final String p_key ) throws UnsupportedEncodingException
        {
            return new SecretKeySpec( p_key.getBytes( "UTF-8" ), m_id );
        }

        /**
         * @param p_key key object
         * @return cipher
         *
         * @throws NoSuchPaddingException on padding error
         * @throws NoSuchAlgorithmException on algorithm error
         * @throws InvalidKeyException on key invalid
         */
        public final Cipher getEncryptCipher( final Key p_key ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
        {
            final Cipher l_cipher = Cipher.getInstance( m_id );
            l_cipher.init( Cipher.ENCRYPT_MODE, p_key );
            return l_cipher;
        }

    }
}
