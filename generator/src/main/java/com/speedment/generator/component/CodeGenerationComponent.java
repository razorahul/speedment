/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.generator.component;

import com.speedment.common.codegen.model.ClassOrInterface;
import com.speedment.common.injector.annotation.InjectorKey;
import com.speedment.generator.Translator;
import com.speedment.generator.TranslatorConstructor;
import com.speedment.generator.TranslatorDecorator;
import com.speedment.generator.TranslatorKey;
import com.speedment.runtime.annotation.Api;
import com.speedment.runtime.component.Component;
import com.speedment.runtime.config.trait.HasMainInterface;
import com.speedment.runtime.config.trait.HasName;
import com.speedment.runtime.exception.SpeedmentException;

import java.util.stream.Stream;

/**
 * This Component interface is used for Speedmens's code generation.
 *
 * @author  Per Minborg
 * @author  Emil Forslund
 * @since   2.2.0
 */
@Api(version = "3.0")
@InjectorKey(CodeGenerationComponent.class)
public interface CodeGenerationComponent extends Component {

    @Override
    default Class<CodeGenerationComponent> getComponentClass() {
        return CodeGenerationComponent.class;
    }

    /**
     * Puts a new {@code TranslatorConstructor} for the given class/key pair. If
     * an old TranslatorConstructor exists for the same class/key pair, it is
     * replaced.
     *
     * @param <DOC>        type of Document
     * @param <T>          type of codegen model
     * @param docType      class of the Document
     * @param tKey         translatorKey to use
     * @param constructor  to use when producing Translators of the specified
     *                     type
     */
    default <DOC extends HasName & HasMainInterface, T extends ClassOrInterface<T>> void 
    put(Class<DOC> docType, TranslatorKey<DOC, T> tKey, TranslatorConstructor<DOC, T> constructor) {
        put(docType, tKey.getTranslatedType(), tKey.getKey(), constructor);
    }

    /**
     * Puts a new {@code TranslatorConstructor} for the given class/key pair. If
     * an old TranslatorConstructor exists for the same class/key pair, it is
     * replaced.
     *
     * @param <DOC>        type of Document
     * @param <T>          type of codegen model
     * @param docType      class of the Document
     * @param modelType    class of the codegen model
     * @param key          key to use
     * @param constructor  to use when producing Translators of the specified
     *                     type
     */
    <DOC extends HasName & HasMainInterface, T extends ClassOrInterface<T>> void 
    put(Class<DOC> docType, Class<T> modelType, String key, TranslatorConstructor<DOC, T> constructor);

    /**
     * Adds a new {@code TranslatorDecorator} for the given class/key pair.
     *
     * @param <DOC>      type of Document
     * @param <T>        type of codegen model
     * @param docType      class of the Document
     * @param tKey       translatorKey to use
     * @param decorator  the new decorator
     */
    default <DOC extends HasName & HasMainInterface, T extends ClassOrInterface<T>> void 
    add(Class<DOC> docType, TranslatorKey<DOC, T> tKey, TranslatorDecorator<DOC, T> decorator) {
        add(docType, tKey.getTranslatedType(), tKey.getKey(), decorator);
    }

    /**
     * Adds a new {@code TranslatorDecorator} for the given class/key pair.
     *
     * @param <DOC>      type of Document
     * @param <T>        type of codegen model
     * @param docType    class of the Document
     * @param modelType  class of the codegen mdoel
     * @param key        key to use
     * @param decorator  the new decorator
     */
    <DOC extends HasName & HasMainInterface, T extends ClassOrInterface<T>> void 
    add(Class<DOC> docType, Class<T> modelType, String key, TranslatorDecorator<DOC, T> decorator);

    /**
     * Removes the {@code TranslatorConstructor} for the given class/key pair.
     *
     * @param <DOC>  type of Document
     * @param <T>    type of codegen model
     * @param docType  class of the Document
     * @param tKey   translatorKey to use
     */
    default <DOC extends HasName & HasMainInterface, T extends ClassOrInterface<T>> void 
    remove(Class<DOC> docType, TranslatorKey<DOC, T> tKey) {
        remove(docType, tKey.getKey());
    }

    /**
     * Removes the {@code TranslatorConstructor} for the given class/key pair.
     *
     * @param <DOC>    type of Document
     * @param <T>      type of codegen model
     * @param docType  class of the Document
     * @param key      key to use
     */
    <DOC extends HasName & HasMainInterface, T extends ClassOrInterface<T>> void 
    remove(Class<DOC> docType, String key);
    
     /**
     * Returns a Stream of newly created {@code Translator Translators} for the
     * given Document. The translators are all created regardless of its
     * registered key.
     *
     * @param <DOC>     Document type
     * @param document  to use when making translators
     * @return          a Stream of newly created {@code Translator Translators} 
     *                  for the given Document
     */
    <DOC extends HasName & HasMainInterface> Stream<? extends Translator<DOC, ?>> 
    translators(DOC document);

    /**
     * Returns a Stream of newly created {@code Translator Translators} for the
     * given Document and key. Only Translators that match the provided key are
     * included in the Stream.
     *
     * @param <DOC>     document type
     * @param <T>       codegen model type
     * @param document  to use when making translators
     * @param key       the key
     * @return          a Stream of newly created {@code Translator Translators} 
     *                  for the given Document
     * 
     * @throws SpeedmentException  if the specified translator did not exist
     */
    default <DOC extends HasName & HasMainInterface, T extends ClassOrInterface<T>> Translator<DOC, T>
    findTranslator(DOC document, TranslatorKey<DOC, T> key) throws SpeedmentException {
        return CodeGenerationComponent.this.findTranslator(document, key.getTranslatedType(), key.getKey());
    }

    /**
     * Returns a Stream of newly created {@code Translator Translators} for the
     * given Document and key. Only Translators that match the provided key are
     * included in the Stream.
     *
     * @param <DOC>      document type
     * @param <T>        codegen model type
     * @param document   to use when making translators
     * @param modelType  type to indicate return type variables
     * @param key        the key
     * @return           the newly created {@code Translator Translators} for the 
     *                   given Document
     * 
     * @throws SpeedmentException  if the specified translator did not exist
     */
    <DOC extends HasName & HasMainInterface, T extends ClassOrInterface<T>> Translator<DOC, T> 
    findTranslator(DOC document, Class<T> modelType, String key) throws SpeedmentException;
    
    /**
     * Returns a stream over the currently installed translator keys.
     * 
     * @return  stream of translator keys
     */
    Stream<String> translatorKeys();
}