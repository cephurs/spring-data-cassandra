/*
 * Copyright 2011-2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.cassandra.core;

import java.util.List;

import org.springframework.cassandra.core.CqlOperations;
import org.springframework.cassandra.core.QueryOptions;
import org.springframework.data.cassandra.convert.CassandraConverter;

import com.datastax.driver.core.querybuilder.Select;

/**
 * Operations for interacting with Cassandra. These operations are used by the Repository implementation, but can also
 * be used directly when that is desired by the developer.
 * 
 * @author Alex Shvid
 * @author David Webb
 * @author Matthew Adams
 * 
 */
public interface CassandraOperations extends CqlOperations {

	/**
	 * The table name used for the specified class by this template.
	 * 
	 * @param entityClass must not be {@literal null}.
	 * @return
	 */
	String getTableName(Class<?> entityClass);

	/**
	 * Execute query and convert ResultSet to the list of entities
	 * 
	 * @param query must not be {@literal null}.
	 * @param selectClass must not be {@literal null}, mapped entity type.
	 * @return
	 */
	<T> List<T> select(String cql, Class<T> selectClass);

	/**
	 * Execute query and convert ResultSet to the list of entities
	 * 
	 * @param selectQuery must not be {@literal null}.
	 * @param selectClass must not be {@literal null}, mapped entity type.
	 * @return
	 */

	<T> List<T> select(Select selectQuery, Class<T> selectClass);

	/**
	 * Execute query and convert ResultSet to the entity
	 * 
	 * @param query must not be {@literal null}.
	 * @param selectClass must not be {@literal null}, mapped entity type.
	 * @return
	 */
	<T> T selectOne(String cql, Class<T> selectClass);

	<T> T selectOne(Select selectQuery, Class<T> selectClass);

	/**
	 * Counts rows for given query
	 * 
	 * @param selectQuery
	 * @return
	 */

	Long count(Select selectQuery);

	/**
	 * Counts all rows for given table
	 * 
	 * @param tableName
	 * @return
	 */

	Long count(String tableName);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param entity
	 */
	<T> T insert(T entity);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param entity
	 * @param tableName
	 * @return
	 */
	<T> T insert(T entity, String tableName);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> T insert(T entity, String tableName, QueryOptions options);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> T insert(T entity, QueryOptions options);

	/**
	 * Insert the given list of objects to the table by annotation table name.
	 * 
	 * @param entities
	 * @return
	 */
	<T> List<T> insert(List<T> entities);

	/**
	 * Insert the given list of objects to the table by name.
	 * 
	 * @param entities
	 * @param tableName
	 * @return
	 */
	<T> List<T> insert(List<T> entities, String tableName);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> List<T> insert(List<T> entities, QueryOptions options);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> List<T> insert(List<T> entities, String tableName, QueryOptions options);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> T insertAsynchronously(T entity);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> T insertAsynchronously(T entity, String tableName);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> T insertAsynchronously(T entity, QueryOptions options);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> T insertAsynchronously(T entity, String tableName, QueryOptions options);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> List<T> insertAsynchronously(List<T> entities);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> List<T> insertAsynchronously(List<T> entities, String tableName);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> List<T> insertAsynchronously(List<T> entities, QueryOptions options);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> List<T> insertAsynchronously(List<T> entities, String tableName, QueryOptions options);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> T update(T entity);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> T update(T entity, String tableName);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> T update(T entity, QueryOptions options);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> T update(T entity, String tableName, QueryOptions options);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> List<T> update(List<T> entities);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> List<T> update(List<T> entities, String tableName);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> List<T> update(List<T> entities, QueryOptions options);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> List<T> update(List<T> entities, String tableName, QueryOptions options);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> T updateAsynchronously(T entity);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> T updateAsynchronously(T entity, String tableName);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> T updateAsynchronously(T entity, QueryOptions options);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> T updateAsynchronously(T entity, String tableName, QueryOptions options);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> List<T> updateAsynchronously(List<T> entities);

	/**
	 * Insert the given object to the table by id.
	 * 
	 * @param object
	 */
	<T> List<T> updateAsynchronously(List<T> entities, String tableName);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> List<T> updateAsynchronously(List<T> entities, QueryOptions options);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 * @return
	 */
	<T> List<T> updateAsynchronously(List<T> entities, String tableName, QueryOptions options);

	/**
	 * Remove the given object from the table by id.
	 * 
	 * @param object
	 */
	<T> void delete(T entity);

	/**
	 * Removes the given object from the given table.
	 * 
	 * @param object
	 * @param table must not be {@literal null} or empty.
	 */
	<T> void delete(T entity, String tableName);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 */
	<T> void delete(T entity, QueryOptions options);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 */
	<T> void delete(T entity, String tableName, QueryOptions options);

	/**
	 * Remove the given object from the table by id.
	 * 
	 * @param object
	 */
	<T> void delete(List<T> entities);

	/**
	 * Removes the given object from the given table.
	 * 
	 * @param object
	 * @param table must not be {@literal null} or empty.
	 */
	<T> void delete(List<T> entities, String tableName);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 */
	<T> void delete(List<T> entities, QueryOptions options);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 */
	<T> void delete(List<T> entities, String tableName, QueryOptions options);

	/**
	 * Remove the given object from the table by id.
	 * 
	 * @param object
	 */
	<T> void deleteAsynchronously(T entity);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 */
	<T> void deleteAsynchronously(T entity, QueryOptions options);

	/**
	 * @param entity
	 * @param tableName
	 * @param options
	 */
	<T> void deleteAsynchronously(T entity, String tableName, QueryOptions options);

	/**
	 * Removes the given object from the given table.
	 * 
	 * @param object
	 * @param table must not be {@literal null} or empty.
	 */
	<T> void deleteAsynchronously(T entity, String tableName);

	/**
	 * Remove the given object from the table by id.
	 * 
	 * @param object
	 */
	<T> void deleteAsynchronously(List<T> entities);

	/**
	 * Removes the given object from the given table.
	 * 
	 * @param object
	 * @param table must not be {@literal null} or empty.
	 */
	<T> void deleteAsynchronously(List<T> entities, String tableName);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 */
	<T> void deleteAsynchronously(List<T> entities, QueryOptions options);

	/**
	 * @param entities
	 * @param tableName
	 * @param options
	 */
	<T> void deleteAsynchronously(List<T> entities, String tableName, QueryOptions options);

	/**
	 * Returns the underlying {@link CassandraConverter}.
	 * 
	 * @return
	 */
	CassandraConverter getConverter();
}
