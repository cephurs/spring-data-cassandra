package org.springframework.data.cassandra.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.cassandra.core.QueryOptions;
import org.springframework.data.cassandra.exception.EntityWriterException;
import org.springframework.data.cassandra.mapping.CassandraPersistentEntity;
import org.springframework.data.cassandra.mapping.CassandraPersistentProperty;
import org.springframework.data.convert.EntityWriter;
import org.springframework.data.mapping.PropertyHandler;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Query;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Delete.Where;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;

/**
 * Utilities to convert Cassandra Annotated objects to Queries and CQL.
 * 
 * @author Alex Shvid
 * @author David Webb
 * @author Matthew T. Adams
 */
public abstract class CqlUtils {

	/**
	 * Create the List of CQL for the indexes required for Cassandra mapped Table.
	 * 
	 * @param tableName
	 * @param entity
	 * @return The list of CQL statements to run with session.execute()
	 */
	public static List<String> createIndexes(final String tableName, final CassandraPersistentEntity<?> entity) {
		final List<String> result = new ArrayList<String>();

		entity.doWithProperties(new PropertyHandler<CassandraPersistentProperty>() {
			@Override
			public void doWithPersistentProperty(CassandraPersistentProperty prop) {

				if (prop.isIndexed()) {

					final StringBuilder str = new StringBuilder();
					str.append("CREATE INDEX ON ");
					str.append(tableName);
					str.append(" (");
					str.append(prop.getColumnName());
					str.append(");");

					result.add(str.toString());
				}

			}
		});

		return result;
	}

	/**
	 * Alter the table to refelct the entity annotations
	 * 
	 * @param tableName
	 * @param entity
	 * @param table
	 * @return
	 */
	public static List<String> alterTable(final String tableName, final CassandraPersistentEntity<?> entity,
			final TableMetadata table) {
		final List<String> result = new ArrayList<String>();

		entity.doWithProperties(new PropertyHandler<CassandraPersistentProperty>() {
			@Override
			public void doWithPersistentProperty(CassandraPersistentProperty prop) {

				String columnName = prop.getColumnName();
				DataType columnDataType = prop.getDataType();
				ColumnMetadata columnMetadata = table.getColumn(columnName.toLowerCase());

				if (columnMetadata != null && columnDataType.equals(columnMetadata.getType())) {
					return;
				}

				final StringBuilder str = new StringBuilder();
				str.append("ALTER TABLE ");
				str.append(tableName);
				if (columnMetadata == null) {
					str.append(" ADD ");
				} else {
					str.append(" ALTER ");
				}

				str.append(columnName);
				str.append(' ');

				if (columnMetadata != null) {
					str.append("TYPE ");
				}

				str.append(toCQL(columnDataType));

				str.append(';');
				result.add(str.toString());

			}
		});

		return result;
	}

	/**
	 * Generates a Query Object for an insert
	 * 
	 * @param tableName
	 * @param objectToSave
	 * @param entity
	 * @param optionsByName
	 * 
	 * @return The Query object to run with session.execute();
	 * @throws EntityWriterException
	 */
	public static Query toInsertQuery(String tableName, final Object objectToSave, QueryOptions options,
			EntityWriter<Object, Object> entityWriter) throws EntityWriterException {

		final Insert q = QueryBuilder.insertInto(tableName);

		/*
		 * Write properties
		 */
		entityWriter.write(objectToSave, q);

		/*
		 * Add Query Options
		 */
		CqlTemplate.addQueryOptions(q, options);

		/*
		 * Add TTL to Insert object
		 */
		if (options != null && options.getTtl() != null) {
			q.using(QueryBuilder.ttl(options.getTtl()));
		}

		return q;

	}

	/**
	 * Generates a Query Object for an Update
	 * 
	 * @param tableName
	 * @param objectToSave
	 * @param entity
	 * @param optionsByName
	 * 
	 * @return The Query object to run with session.execute();
	 * @throws EntityWriterException
	 */
	public static Query toUpdateQuery(String tableName, final Object objectToSave, QueryOptions options,
			EntityWriter<Object, Object> entityWriter) throws EntityWriterException {

		final Update q = QueryBuilder.update(tableName);

		/*
		 * Write properties
		 */
		entityWriter.write(objectToSave, q);

		/*
		 * Add Query Options
		 */
		CqlTemplate.addQueryOptions(q, options);

		/*
		 * Add TTL to Insert object
		 */
		if (options != null && options.getTtl() != null) {
			q.using(QueryBuilder.ttl(options.getTtl()));
		}

		return q;

	}

	/**
	 * Generates a Batch Object for multiple Updates
	 * 
	 * @param tableName
	 * @param objectsToSave
	 * @param entity
	 * @param optionsByName
	 * 
	 * @return The Query object to run with session.execute();
	 * @throws EntityWriterException
	 */
	public static <T> Batch toUpdateBatchQuery(final String tableName, final List<T> objectsToSave, QueryOptions options,
			EntityWriter<Object, Object> entityWriter) throws EntityWriterException {

		/*
		 * Return variable is a Batch statement
		 */
		final Batch b = QueryBuilder.batch();

		for (final T objectToSave : objectsToSave) {

			b.add((Statement) toUpdateQuery(tableName, objectToSave, options, entityWriter));

		}

		/*
		 * Add Query Options
		 */
		CqlTemplate.addQueryOptions(b, options);

		return b;

	}

	/**
	 * Generates a Batch Object for multiple inserts
	 * 
	 * @param tableName
	 * @param objectsToSave
	 * @param entity
	 * @param optionsByName
	 * 
	 * @return The Query object to run with session.execute();
	 * @throws EntityWriterException
	 */
	public static <T> Batch toInsertBatchQuery(final String tableName, final List<T> objectsToSave, QueryOptions options,
			EntityWriter<Object, Object> entityWriter) throws EntityWriterException {

		/*
		 * Return variable is a Batch statement
		 */
		final Batch b = QueryBuilder.batch();

		for (final T objectToSave : objectsToSave) {

			b.add((Statement) toInsertQuery(tableName, objectToSave, options, entityWriter));

		}

		/*
		 * Add Query Options
		 */
		CqlTemplate.addQueryOptions(b, options);

		return b;

	}

	/**
	 * Create a Delete Query Object from an annotated POJO
	 * 
	 * @param tableName
	 * @param objectToRemove
	 * @param entity
	 * @param optionsByName
	 * @return
	 * @throws EntityWriterException
	 */
	public static Query toDeleteQuery(String tableName, final Object objectToRemove, QueryOptions options,
			EntityWriter<Object, Object> entityWriter) throws EntityWriterException {

		final Delete.Selection ds = QueryBuilder.delete();
		final Delete q = ds.from(tableName);
		final Where w = q.where();

		/*
		 * Write where condition to find by Id
		 */
		entityWriter.write(objectToRemove, w);

		CqlTemplate.addQueryOptions(q, options);

		return q;

	}

	/**
	 * @param dataType
	 * @return
	 */
	public static String toCQL(DataType dataType) {
		if (dataType.getTypeArguments().isEmpty()) {
			return dataType.getName().name();
		} else {
			StringBuilder str = new StringBuilder();
			str.append(dataType.getName().name());
			str.append('<');
			for (DataType argDataType : dataType.getTypeArguments()) {
				if (str.charAt(str.length() - 1) != '<') {
					str.append(',');
				}
				str.append(argDataType.getName().name());
			}
			str.append('>');
			return str.toString();
		}
	}

	/**
	 * @param tableName
	 * @return
	 */
	public static String dropTable(String tableName) {

		if (tableName == null) {
			return null;
		}

		StringBuilder str = new StringBuilder();
		str.append("DROP TABLE " + tableName + ";");
		return str.toString();
	}

	/**
	 * Create a Batch Query object for multiple deletes.
	 * 
	 * @param tableName
	 * @param entities
	 * @param entity
	 * @param optionsByName
	 * 
	 * @return
	 * @throws EntityWriterException
	 */
	public static <T> Batch toDeleteBatchQuery(String tableName, List<T> entities, QueryOptions options,
			EntityWriter<Object, Object> entityWriter) throws EntityWriterException {

		/*
		 * Return variable is a Batch statement
		 */
		final Batch b = QueryBuilder.batch();

		for (final T objectToSave : entities) {

			b.add((Statement) toDeleteQuery(tableName, objectToSave, options, entityWriter));

		}

		CqlTemplate.addQueryOptions(b, options);

		return b;

	}

}
