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
package com.l2jbr.gameserver.serverpackets;

import com.l2jbr.gameserver.network.L2GameClient;
import com.l2jbr.mmocore.SendablePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author KenM
 */
public abstract class L2GameServerPacket extends SendablePacket<L2GameClient>
{
	private static final Logger _log = LoggerFactory.getLogger(L2GameServerPacket.class.getName());
	
	@Override
	protected void write()
	{
		try
		{
			writeImpl();
		}
		catch (Throwable t)
		{
			_log.error("Client: " + getClient().toString() + " - Failed writing: " + getType() + ";");
			t.printStackTrace();
		}
	}
	
	public void runImpl()
	{
		
	}
	
	protected abstract void writeImpl();
	
	/**
	 * @return A String with this packet name for debugging purposes
	 */
	public abstract String getType();
}
