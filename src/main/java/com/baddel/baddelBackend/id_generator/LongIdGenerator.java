package com.baddel.baddelBackend.id_generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class LongIdGenerator implements IdentifierGenerator {
    public static final String generatorName = "longIdGenerator";

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) throws HibernateException {
        Long timestamp = System.currentTimeMillis();
        Random random = new Random();
        Long randomNumber = random.nextLong(10000000L);
        String concatenatedString = String.valueOf(timestamp) + String.valueOf(randomNumber);
        concatenatedString = concatenatedString.substring(concatenatedString.length()-14);
        return Long.parseLong(concatenatedString);
    }
}
