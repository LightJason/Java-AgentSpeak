/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.crypto;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.error.CEnumConstantNotPresentException;

import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


/**
 * enum with encrypting types
 */
public enum ECryptAlgorithm
{
    AES( "AES/ECB/PKCS5Padding", "AES" ),
    DES( "DES/ECB/PKCS5Padding", "DES" ),
    RSA( "RSA/ECB/PKCS1Padding", "RSA" );

    /**
     * chipher name
     */
    private final String m_cipher;
    /**
     * key name
     */
    private final String m_key;

    /**
     * ctor
     *
     * @param p_cipher chipher name
     * @param p_key name of the key
     */
    ECryptAlgorithm( @Nonnull final String p_cipher, @Nonnull final String p_key )
    {
        m_cipher = p_cipher;
        m_key = p_key;
    }

    /**
     * generates a key
     *
     * @return key pair object (public key / private key or null)
     *
     * @throws NoSuchAlgorithmException on algorithm error
     */
    @Nonnull
    public Pair<Key, Key> generateKey() throws NoSuchAlgorithmException
    {
        switch ( this )
        {
            case AES:
            case DES:
                return new ImmutablePair<>( KeyGenerator.getInstance( m_key ).generateKey(), null );

            case RSA:
                final KeyPair l_key = KeyPairGenerator.getInstance( m_key ).generateKeyPair();
                return new ImmutablePair<>( l_key.getPublic(), l_key.getPrivate() );

            default:
                throw new CEnumConstantNotPresentException( this.getClass(), this.toString() );
        }
    }

    /**
     * returns encrypt cipher
     *
     * @param p_key key object
     * @return cipher
     *
     * @throws NoSuchPaddingException on padding error
     * @throws NoSuchAlgorithmException on algorithm error
     * @throws InvalidKeyException on key invalid
     */
    @Nonnull
    public Cipher getEncryptCipher( @Nonnull final Key p_key ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        final Cipher l_cipher = Cipher.getInstance( m_cipher );
        l_cipher.init( Cipher.ENCRYPT_MODE, p_key );
        return l_cipher;
    }

    /**
     * * returns decrypt cipher
     *
     * @param p_key key object
     * @return cipher
     *
     * @throws NoSuchPaddingException on padding error
     * @throws NoSuchAlgorithmException on algorithm error
     * @throws InvalidKeyException on key invalid
     */
    @Nonnull
    public Cipher getDecryptCipher( @Nonnull final Key p_key ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
    {
        final Cipher l_cipher = Cipher.getInstance( m_cipher );
        l_cipher.init( Cipher.DECRYPT_MODE, p_key );
        return l_cipher;
    }

    /**
     * additional factory
     *
     * @param p_value string value
     * @return enum
     */
    @Nonnull
    public static ECryptAlgorithm of( @Nonnull final String p_value )
    {
        return ECryptAlgorithm.valueOf( p_value.trim().toUpperCase( Locale.ROOT ) );
    }

}
