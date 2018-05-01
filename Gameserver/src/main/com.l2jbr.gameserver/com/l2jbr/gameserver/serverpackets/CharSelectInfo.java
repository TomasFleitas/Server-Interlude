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
package com.l2jbr.gameserver.serverpackets;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.commons.database.L2DatabaseFactory;
import com.l2jbr.gameserver.model.CharSelectInfoPackage;
import com.l2jbr.gameserver.model.Inventory;
import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.database.Character;
import com.l2jbr.gameserver.model.database.repository.AugmentationsRepository;
import com.l2jbr.gameserver.model.database.repository.CharacterRepository;
import com.l2jbr.gameserver.model.database.repository.CharacterSubclassesRepository;
import com.l2jbr.gameserver.network.L2GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * This class ...
 *
 * @version $Revision: 1.8.2.4.2.6 $ $Date: 2005/04/06 16:13:46 $
 */
public class CharSelectInfo extends L2GameServerPacket {
    // d SdSddddddddddffddddddddddddddddddddddddddddddddddddddddddddddffd
    private static final String _S__1F_CHARSELECTINFO = "[S] 1F CharSelectInfo";

    private static Logger _log = LoggerFactory.getLogger(CharSelectInfo.class.getName());

    private final String _loginName;

    private final int _sessionId;

    private int _activeId;

    private final CharSelectInfoPackage[] _characterPackages;

    /**
     * @param loginName
     * @param sessionId
     */
    public CharSelectInfo(String loginName, int sessionId) {
        _sessionId = sessionId;
        _loginName = loginName;
        _characterPackages = loadCharacterSelectInfo();
        _activeId = -1;
    }

    public CharSelectInfo(String loginName, int sessionId, int activeId) {
        _sessionId = sessionId;
        _loginName = loginName;
        _characterPackages = loadCharacterSelectInfo();
        _activeId = activeId;
    }

    public CharSelectInfoPackage[] getCharInfo() {
        return _characterPackages;
    }

    @Override
    protected final void writeImpl() {
        int size = (_characterPackages.length);

        writeC(0x13);
        writeD(size);

        long lastAccess = 0L;

        if (_activeId == -1) {
            for (int i = 0; i < size; i++) {
                if (lastAccess < _characterPackages[i].getLastAccess()) {
                    lastAccess = _characterPackages[i].getLastAccess();
                    _activeId = i;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            CharSelectInfoPackage charInfoPackage = _characterPackages[i];

            writeS(charInfoPackage.getName());
            writeD(charInfoPackage.getCharId());
            writeS(_loginName);
            writeD(_sessionId);
            writeD(charInfoPackage.getClanId());
            writeD(0x00); // ??

            writeD(charInfoPackage.getSex());
            writeD(charInfoPackage.getRace());

            if (charInfoPackage.getClassId() == charInfoPackage.getBaseClassId()) {
                writeD(charInfoPackage.getClassId());
            } else {
                writeD(charInfoPackage.getBaseClassId());
            }

            writeD(0x01); // active ??

            writeD(0x00); // x
            writeD(0x00); // y
            writeD(0x00); // z

            writeF(charInfoPackage.getCurrentHp()); // hp cur
            writeF(charInfoPackage.getCurrentMp()); // mp cur

            writeD(charInfoPackage.getSp());
            writeQ(charInfoPackage.getExp());
            writeD(charInfoPackage.getLevel());

            writeD(charInfoPackage.getKarma()); // karma
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);

            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_DHAIR));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LRHAND));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
            writeD(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_FACE));

            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_DHAIR));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_REAR));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LEAR));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_NECK));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_FEET));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_BACK));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LRHAND));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
            writeD(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_FACE));

            writeD(charInfoPackage.getHairStyle());
            writeD(charInfoPackage.getHairColor());
            writeD(charInfoPackage.getFace());

            writeF(charInfoPackage.getMaxHp()); // hp max
            writeF(charInfoPackage.getMaxMp()); // mp max

            long deleteTime = charInfoPackage.getDeleteTimer();
            int deletedays = 0;
            if (deleteTime > 0) {
                deletedays = (int) ((deleteTime - System.currentTimeMillis()) / 1000);
            }
            writeD(deletedays); // days left before
            // delete .. if != 0
            // then char is inactive
            writeD(charInfoPackage.getClassId());
            if (i == _activeId) {
                writeD(0x01);
            } else {
                writeD(0x00); // c3 auto-select char
            }

            writeC(charInfoPackage.getEnchantEffect() > 127 ? 127 : charInfoPackage.getEnchantEffect());

            writeD(charInfoPackage.getAugmentationId());
        }
    }

    private CharSelectInfoPackage[] loadCharacterSelectInfo() {
        List<CharSelectInfoPackage> characterList = new LinkedList<>();

        CharacterRepository repository = DatabaseAccess.getRepository(CharacterRepository.class);
        repository.findAllByAccountName(_loginName).forEach(character -> {
            CharSelectInfoPackage charInfoPackage = restoreChar(character);
            if (charInfoPackage != null) {
                characterList.add(charInfoPackage);
            }
        });

        return characterList.toArray(new CharSelectInfoPackage[characterList.size()]);

    }

    private void loadCharacterSubclassInfo(CharSelectInfoPackage charInfopackage, int ObjectId, int activeClassId) {
        CharacterSubclassesRepository repository = DatabaseAccess.getRepository(CharacterSubclassesRepository.class);
        repository.findByClassId(ObjectId, activeClassId).ifPresent(characterSubclasse -> {
            charInfopackage.setExp(characterSubclasse.getExp());
            charInfopackage.setSp(characterSubclasse.getSp());
            charInfopackage.setLevel(characterSubclasse.getLevel());
        });
    }

    private CharSelectInfoPackage restoreChar(Character character) {
        int objectId = character.getObjectId();

        // See if the char must be deleted
        long deleteTime = character.getDeleteTime();
        if (deleteTime > 0) {
            if (System.currentTimeMillis() > deleteTime) {
                L2PcInstance cha = L2PcInstance.load(objectId);
                L2Clan clan = cha.getClan();
                if (clan != null) {
                    clan.removeClanMember(cha.getName(), 0);
                }

                L2GameClient.deleteCharByObjId(objectId);
                return null;
            }
        }

        String name = character.getCharName();

        CharSelectInfoPackage charInfopackage = new CharSelectInfoPackage(objectId, name);
        charInfopackage.setLevel(character.getLevel());
        charInfopackage.setMaxHp(character.getMaxHp());
        charInfopackage.setCurrentHp(character.getCurrentHp());
        charInfopackage.setMaxMp(character.getMaxMp());
        charInfopackage.setCurrentMp(character.getCurrentMp());
        charInfopackage.setKarma(character.getKarma());

        charInfopackage.setFace(character.getFace());
        charInfopackage.setHairStyle(character.getHairStyle());
        charInfopackage.setHairColor(character.getHairColor());
        charInfopackage.setSex(character.getSex());

        charInfopackage.setExp(character.getExperience());
        charInfopackage.setSp(character.getSp());
        charInfopackage.setClanId(character.getClanId());

        charInfopackage.setRace(character.getRace());

        final int baseClassId = character.getBaseClass();
        final int activeClassId = character.getClassId();

        // if is in subclass, load subclass exp, sp, lvl info
        if (baseClassId != activeClassId) {
            loadCharacterSubclassInfo(charInfopackage, objectId, activeClassId);
        }

        charInfopackage.setClassId(activeClassId);

        // Get the augmentation id for equipped weapon
        int weaponObjId = charInfopackage.getPaperdollObjectId(Inventory.PAPERDOLL_LRHAND);
        if (weaponObjId < 1) {
            weaponObjId = charInfopackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND);
        }

        if (weaponObjId > 0) {
            AugmentationsRepository repository = DatabaseAccess.getRepository(AugmentationsRepository.class);
            repository.findById(weaponObjId).ifPresent(augmentation -> {
                charInfopackage.setAugmentationId(augmentation.getAttributes());
            });
        }

        /*
         * Check if the base class is set to zero and alse doesn't match with the current active class, otherwise send the base class ID. This prevents chars created before base class was introduced from being displayed incorrectly.
         */
        if ((baseClassId == 0) && (activeClassId > 0)) {
            charInfopackage.setBaseClassId(activeClassId);
        } else {
            charInfopackage.setBaseClassId(baseClassId);
        }

        charInfopackage.setDeleteTimer(deleteTime);
        charInfopackage.setLastAccess(character.getLastAccess());

        return charInfopackage;
    }

    @Override
    public String getType() {
        return _S__1F_CHARSELECTINFO;
    }
}
