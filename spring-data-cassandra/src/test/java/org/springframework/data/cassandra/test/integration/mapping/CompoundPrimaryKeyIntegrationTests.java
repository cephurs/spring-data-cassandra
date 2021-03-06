/*
 * Copyright (c) 2011 by the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.cassandra.test.integration.mapping;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.cassandra.mapping.BasicCassandraPersistentEntity;
import org.springframework.data.cassandra.mapping.BasicCassandraPersistentProperty;
import org.springframework.data.cassandra.mapping.CassandraPersistentEntity;
import org.springframework.data.cassandra.mapping.CassandraPersistentProperty;
import org.springframework.data.cassandra.mapping.CassandraSimpleTypeHolder;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.util.ReflectionUtils;

/**
 * Integration test for {@link BasicCassandraPersistentProperty}.
 * 
 * @author Alex Shvid
 */
public class CompoundPrimaryKeyIntegrationTests {

	@PrimaryKeyClass
	static class TimelineKey {

		@PrimaryKeyColumn(ordinal = 0)
		String id;

		@PrimaryKeyColumn(ordinal = 1)
		Date dt;
	}

	static class Timeline {

		@PrimaryKey
		TimelineKey id;

		@Column("message")
		String text;

	}

	CassandraPersistentEntity<Timeline> entity;

	@Before
	public void setup() {
		entity = new BasicCassandraPersistentEntity<Timeline>(ClassTypeInformation.from(Timeline.class));
	}

	@Test
	public void checksIdProperty() {
		Field field = ReflectionUtils.findField(Timeline.class, "id");
		CassandraPersistentProperty property = getPropertyFor(field);
		assertTrue(property.isIdProperty());
	}

	private CassandraPersistentProperty getPropertyFor(Field field) {
		return new BasicCassandraPersistentProperty(field, null, entity, new CassandraSimpleTypeHolder());
	}
}
