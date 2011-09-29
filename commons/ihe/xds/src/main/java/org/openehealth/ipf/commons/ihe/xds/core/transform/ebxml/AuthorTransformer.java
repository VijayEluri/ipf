/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import static org.apache.commons.lang3.Validate.notNull;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Author;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Organization;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Person;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.*;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.OrganizationTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.PersonTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms between an {@link Author} instance and its representation in ebXML.
 * @author Jens Riemschneider
 */
public class AuthorTransformer {
    private final PersonTransformer personTransformer = new PersonTransformer();
    private final OrganizationTransformer organizationTransformer = new OrganizationTransformer();
    private final EbXMLFactory factory;
    
    /**
     * Constructs the transformer
     * @param ebXMLFactory
     *          factory for version independent ebXML objects. 
     */
    public AuthorTransformer(EbXMLFactory ebXMLFactory) {
        notNull(ebXMLFactory, "ebXMLFactory cannot be null");
        factory = ebXMLFactory;
    }

    /**
     * Transforms an {@link Author} to a {@link EbXMLClassification}.
     * @param author
     *          the author. Can be <code>null</code>.
     * @param objectLibrary
     *          the object library to use.
     * @return the classification. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLClassification toEbXML(Author author, EbXMLObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objectLibrary cannot be null");
        
        if (author == null) {
            return null;
        }
        
        EbXMLClassification classification = factory.createClassification(objectLibrary);
        classification.setNodeRepresentation("");

        String hl7XCN = personTransformer.toHL7(author.getAuthorPerson());
        if (hl7XCN != null) {
            classification.addSlot(SLOT_NAME_AUTHOR_PERSON, hl7XCN);
        }

        List<String> hl7XONs = new ArrayList<String>();
        for (Organization organization : author.getAuthorInstitution()) {
            String hl7 = organizationTransformer.toHL7(organization);
            if (hl7 != null) {
                hl7XONs.add(hl7);
            }
        }
        
        classification.addSlot(SLOT_NAME_AUTHOR_INSTITUTION,
                hl7XONs.toArray(new String[hl7XONs.size()]));
        
        classification.addSlot(SLOT_NAME_AUTHOR_ROLE,
                author.getAuthorRole().toArray(new String[author.getAuthorRole().size()]));

        classification.addSlot(SLOT_NAME_AUTHOR_SPECIALTY,
                author.getAuthorSpecialty().toArray(new String[author.getAuthorSpecialty().size()]));
        
        return classification;
    }
    
    /**
     * Transforms an a {@link EbXMLClassification} to {@link Author}. 
     * @param classification
     *          the classification. Can be <code>null</code>.
     * @return the author. <code>null</code> if the input was <code>null</code>.
     */
    public Author fromEbXML(EbXMLClassification classification) {
        if (classification == null) {
            return null;        
        }
        
        List<String> persons = classification.getSlotValues(SLOT_NAME_AUTHOR_PERSON);
        List<String> institutions = classification.getSlotValues(SLOT_NAME_AUTHOR_INSTITUTION);
        List<String> roles = classification.getSlotValues(SLOT_NAME_AUTHOR_ROLE);
        List<String> specialties = classification.getSlotValues(SLOT_NAME_AUTHOR_SPECIALTY);
        
        Person person = null;
        if (persons.size() > 0 && persons.get(0) != null) {
            person = personTransformer.fromHL7(persons.get(0));
        }
        
        Author author = new Author();        
        author.setAuthorPerson(person);
        
        for (String hl7XON : institutions) {
            Organization org = organizationTransformer.fromHL7(hl7XON);
            if (org != null) {
                author.getAuthorInstitution().add(org);
            }
        }
        author.getAuthorRole().addAll(roles);
        author.getAuthorSpecialty().addAll(specialties);
        
        return author;
    }
}
