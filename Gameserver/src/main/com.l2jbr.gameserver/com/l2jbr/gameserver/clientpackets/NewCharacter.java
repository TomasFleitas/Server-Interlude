/*
 * This program is free software; you can redistribute it and/or modify
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
package com.l2jbr.gameserver.clientpackets;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.datatables.CharTemplateTable;
import com.l2jbr.gameserver.model.base.ClassId;
import com.l2jbr.gameserver.serverpackets.CharTemplates;
import com.l2jbr.gameserver.templates.L2PcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class ...
 * @version $Revision: 1.3.4.5 $ $Date: 2005/03/27 15:29:30 $
 */
public final class NewCharacter extends L2GameClientPacket
{
	private static final String _C__0E_NEWCHARACTER = "[C] 0E NewCharacter";
	private static Logger _log = LoggerFactory.getLogger(NewCharacter.class.getName());
	
	@Override
	protected void readImpl()
	{
		
	}
	
	@Override
	protected void runImpl()
	{
		if (Config.DEBUG)
		{
			_log.debug("CreateNewChar");
		}
		
		CharTemplates ct = new CharTemplates();
		
		L2PcTemplate template = CharTemplateTable.getInstance().getTemplate(0);
		ct.addChar(template);
		
		template = CharTemplateTable.getInstance().getTemplate(ClassId.fighter); // human fighter
		ct.addChar(template);
		
		template = CharTemplateTable.getInstance().getTemplate(ClassId.mage); // human mage
		ct.addChar(template);
		
		template = CharTemplateTable.getInstance().getTemplate(ClassId.elvenFighter); // elf fighter
		ct.addChar(template);
		
		template = CharTemplateTable.getInstance().getTemplate(ClassId.elvenMage); // elf mage
		ct.addChar(template);
		
		template = CharTemplateTable.getInstance().getTemplate(ClassId.darkFighter); // dark elf fighter
		ct.addChar(template);
		
		template = CharTemplateTable.getInstance().getTemplate(ClassId.darkMage); // dark elf mage
		ct.addChar(template);
		
		template = CharTemplateTable.getInstance().getTemplate(ClassId.orcFighter); // orc fighter
		ct.addChar(template);
		
		template = CharTemplateTable.getInstance().getTemplate(ClassId.orcMage); // orc mage
		ct.addChar(template);
		
		template = CharTemplateTable.getInstance().getTemplate(ClassId.dwarvenFighter); // dwarf fighter
		ct.addChar(template);
		
		sendPacket(ct);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jbr.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__0E_NEWCHARACTER;
	}
}
