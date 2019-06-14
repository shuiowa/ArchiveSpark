/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2018 Helge Holzmann (L3S) and Vinay Goel (Internet Archive)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.archive.archivespark.functions

import org.archive.archivespark.model._
import org.archive.archivespark.model.dataloads.ByteLoad
import org.archive.archivespark.model.pointers.DependentFieldPointer
import org.archive.archivespark.sparkling.html.HtmlProcessor

object FastHtmlAttributeNamespace {
  def get: DependentFieldPointer[ByteLoad.Root, String] = Html.mapIdentity("attributes").get[String]("attributes")
}

object FastHtmlAttribute {
  def apply(name: String): FastHtmlAttribute = new FastHtmlAttribute(name)
}

class FastHtmlAttribute private (attribute: String) extends BoundEnrichFunc[ByteLoad.Root, String, String](FastHtmlAttributeNamespace.get) {
  override def fields: Seq[String] = Seq(attribute)

  override def derive(source: TypedEnrichable[String], derivatives: Derivatives): Unit = {
    val attrValue = HtmlProcessor.attributeValue(source.get, attribute)
    if (attrValue.isDefined) derivatives << attrValue.get
  }
}