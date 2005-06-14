/*
 * Copyright 2004-2005 The Apache Software Foundation.
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

package org.apache.tiles.xmlDefinition;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;

public class XmlListAttribute extends XmlAttribute {
    /** List.
     * We declare a List to avoid cast.
     * Parent "value" property points to the same list.
     */
  private List list;

    /**
     * Constructor.
     */
  public XmlListAttribute()
    {
    list = new ArrayList();
    setValue(list);
    }

    /**
     * Constructor.
     * @param name Name.
     * @param value List.
     */
  public XmlListAttribute( String name, List value)
    {
    super( name, value );
    list = value;
    }

    /**
     * Add an element in list.
     * We use a property to avoid rewriting a new class.
     * @param element XmlAttribute to add.
     */
  public void add( XmlAttribute element )
    {
    list.add( element.getValue() );
    }

    /**
     * Add an element in list.
     * @param value Object to add.
     */
  public void add( Object value )
    {
    //list.add( value );
      // To correct a bug in digester, we need to check the object type
      // Digester doesn't call correct method according to object type ;-(
    if(value instanceof XmlAttribute)
      {
      add((XmlAttribute)value);
      return;
      }
     else
      list.add( value );
    }

    /**
     * Add an element in list.
     * @param value Object to add.
     */
  public void addObject( Object value )
    {
    list.add( value );
    }



}
