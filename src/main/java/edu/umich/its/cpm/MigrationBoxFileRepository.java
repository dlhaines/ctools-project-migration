package edu.umich.its.cpm;

import java.util.List;
import java.sql.Timestamp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface MigrationBoxFileRepository extends CrudRepository<MigrationBoxFile, String> {
	/**
	 * validate the database connection
	 *
	 * @return 1 if database connection works as expected
	 */
	@Query("SELECT count(*) from MigrationBoxFile")
	public int validate();

	/**
	 * Finds next unprocessed Box file migration request
	 * select the files with least file size first
	 * @return
	 */
	@Query("SELECT bFile FROM MigrationBoxFile bFile WHERE bFile.start_time is null order by bFile.file_size asc")
	public List<MigrationBoxFile> findNextNewMigrationBoxFile();
	
	/**
	 * set start time for given migration Box file record
	 * @param id
	 * @param t
	 */
	@Transactional
	@Modifying(clearAutomatically = false)
	@Query("update MigrationBoxFile bFile set bFile.start_time = ?#{[1]} where bFile.id = ?#{[0]}")
	public void setMigrationBoxFileStartTime(String id, Timestamp t);
	
	/**
	 * set end time for given migration Box file record
	 * @param id
	 * @param t
	 */
	@Transactional
	@Modifying(clearAutomatically = false)
	@Query("update MigrationBoxFile bFile set bFile.end_time = ?#{[1]} where bFile.id = ?#{[0]}")
	public void setMigrationBoxFileEndTime(String id, Timestamp t);
	
	/**
	 * set status for given migration Box file record
	 * @param id
	 * @param status
	 */
	@Transactional
	@Modifying(clearAutomatically = false)
	@Query("update MigrationBoxFile bFile set bFile.status = ?#{[1]} where bFile.id = ?#{[0]}")
	public void setMigrationBoxFileStatus(String id, String status);
	
	/**
	 * select all box file items for certain migration record
	 * @param migrationId
	 * @return
	 */
	@Query("select count(*) from MigrationBoxFile bFile where bFile.migration_id= ?#{[0]}")
	public int getMigrationBoxFileCountForMigration(String migrationId);
	
	/**
	 * select all finished box file items for certain migration record
	 * @param migrationId
	 * @return
	 */
	@Query("select count(*) from MigrationBoxFile bFile where bFile.migration_id= ?#{[0]} and bFile.end_time is not null")
	public int getFinishedMigrationBoxFileCountForMigration(String migrationId);
	
	/**
	 * select the last item migration time for given migration
	 * @param migrationId
	 * @return
	 */
	@Query("SELECT MAX(bFile.end_time) FROM MigrationBoxFile bFile where bFile.migration_id= ?#{[0]}")
	public Timestamp getLastItemEndTimeForMigration(String migrationId);
	
	/**
	 * select all item status for given migration
	 * @param migrationId
	 * @return
	 */
	@Query("SELECT bFile FROM MigrationBoxFile bFile WHERE bFile.migration_id= ?#{[0]} order by bFile.start_time asc")
	public List<MigrationBoxFile> getAllItemStatusForMigration(String migrationId);
	
}