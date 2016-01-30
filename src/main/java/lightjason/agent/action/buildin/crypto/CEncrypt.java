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
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


/**
 * encrypting algorithm
 *
 * @bug not working see http://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption/992413#992413 /
 * http://stackoverflow.com/questions/1205135/how-to-encrypt-string-in-java
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
            final String l_type = CCommon.getRawValue( p_argument.get( 0 ) );
            final Cipher l_cipher = Cipher.getInstance( l_type );
            l_cipher.init( Cipher.ENCRYPT_MODE, new SecretKeySpec( CCommon.<String, ITerm>getRawValue( p_argument.get( 1 ) ).getBytes(), l_type ) );
            p_return.add(
                    CRawTerm.from( DatatypeConverter.printHexBinary( l_cipher.doFinal( CCommon.getBytes( p_argument.subList( 3, p_argument.size() ) ) ) ) ) );

            return CBoolean.from( true );
        }
        catch ( final InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException p_exception )
        {
            return CBoolean.from( false );
        }
    }

}
