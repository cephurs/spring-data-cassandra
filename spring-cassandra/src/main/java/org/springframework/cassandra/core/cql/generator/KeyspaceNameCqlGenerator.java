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
package org.springframework.cassandra.core.cql.generator;

import org.springframework.cassandra.core.keyspace.KeyspaceActionSpecification;
import org.springframework.util.Assert;

public abstract class KeyspaceNameCqlGenerator<T extends KeyspaceActionSpecification<T>> {

	public abstract StringBuilder toCql(StringBuilder cql);

	private KeyspaceActionSpecification<T> specification;

	public KeyspaceNameCqlGenerator(KeyspaceActionSpecification<T> specification) {
		setSpecification(specification);
	}

	protected void setSpecification(KeyspaceActionSpecification<T> specification) {
		Assert.notNull(specification);
		this.specification = specification;
	}

	@SuppressWarnings("unchecked")
	public T getSpecification() {
		return (T) specification;
	}

	/**
	 * Convenient synonymous method of {@link #getSpecification()}.
	 */
	protected T spec() {
		return getSpecification();
	}

	public String toCql() {
		return toCql(null).toString();
	}
}
