package com.l2jbr.gameserver.model.database.repository;

import com.l2jbr.gameserver.model.database.RaidbossSpawnList;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RaidBossSpawnListRepository extends CrudRepository<RaidbossSpawnList, Integer> {

    @Modifying
    @Query("UPDATE raidboss_spawnlist set respawn_time=:respawn, currentHP=:hp, currentMP=:mp where boss_id=:boss")
    int updateById(@Param("boss") Integer bossId, @Param("respawn") long respawnTime, @Param("hp") double hp, @Param("mp") double mp);
}
