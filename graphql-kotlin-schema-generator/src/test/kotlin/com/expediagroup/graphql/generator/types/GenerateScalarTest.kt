/*
 * Copyright 2019 Expedia, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.expediagroup.graphql.generator.types

import com.expediagroup.graphql.types.ID
import graphql.Scalars
import graphql.schema.GraphQLScalarType
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.test.assertEquals

class GenerateScalarTest : TypeTestHelper() {

    @Test
    fun `test all types`() {
        verify(Int::class.createType(), Scalars.GraphQLInt)
        verify(Long::class.createType(), Scalars.GraphQLLong)
        verify(Short::class.createType(), Scalars.GraphQLShort)
        verify(Float::class.createType(), Scalars.GraphQLFloat)
        verify(Double::class.createType(), Scalars.GraphQLFloat)
        verify(BigDecimal::class.createType(), Scalars.GraphQLBigDecimal)
        verify(BigInteger::class.createType(), Scalars.GraphQLBigInteger)
        verify(Char::class.createType(), Scalars.GraphQLChar)
        verify(String::class.createType(), Scalars.GraphQLString)
        verify(Boolean::class.createType(), Scalars.GraphQLBoolean)
        verify(ID::class.createType(), Scalars.GraphQLID)
        verify(IntArray::class.createType(), null)
    }

    private fun verify(kType: KType, expected: GraphQLScalarType?) {
        val actual = generateScalar(generator, kType)
        assertEquals(expected = expected, actual = actual)
    }
}
