/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.loginserver;

import com.l2jbr.loginserver.L2LoginClient.LoginClientState;
import com.l2jbr.loginserver.clientpackets.AuthGameGuard;
import com.l2jbr.loginserver.clientpackets.RequestAuthLogin;
import com.l2jbr.loginserver.clientpackets.RequestServerList;
import com.l2jbr.loginserver.clientpackets.RequestServerLogin;
import com.l2jbr.mmocore.DataWrapper;
import com.l2jbr.mmocore.IPacketHandler;
import com.l2jbr.mmocore.ReceivablePacket;

import java.nio.ByteBuffer;

/**
 * Handler for packets received by Login Server
 * @author KenM
 */
public final class L2LoginPacketHandler implements IPacketHandler<L2LoginClient>
{
	@Override
	public ReceivablePacket<L2LoginClient> handlePacket(ByteBuffer buf, L2LoginClient client) {
		return null;
	}

	@Override
	public ReceivablePacket<L2LoginClient> handlePacket(DataWrapper data, L2LoginClient client) {
        int opcode = Byte.toUnsignedInt(data.get());

        ReceivablePacket<L2LoginClient> packet = null;
        LoginClientState state = client.getState();

        switch (state)
        {
            case CONNECTED:
                if (opcode == 0x07)
                {
                    packet = new AuthGameGuard();
                }
                else
                {
                    debugOpcode(opcode, state);
                }
                break;
            case AUTHED_GG:
                if (opcode == 0x00)
                {
                    packet = new RequestAuthLogin();
                }
                else
                {
                    debugOpcode(opcode, state);
                }
                break;
            case AUTHED_LOGIN:
                if (opcode == 0x05)
                {
                    packet = new RequestServerList();
                }
                else if (opcode == 0x02)
                {
                    packet = new RequestServerLogin();
                }
                else
                {
                    debugOpcode(opcode, state);
                }
                break;
        }
        return packet;
	}

	private void debugOpcode(int opcode, LoginClientState state)
	{
		System.out.println("Unknown Opcode: " + opcode + " for state: " + state.name());
	}
}
