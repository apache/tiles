<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Convert Tag Library Documentation into Tag Library Descriptor -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">

  <!-- Output method and formatting -->
  <xsl:output
             method="xml"
             indent="yes"
     doctype-public="-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN"
     doctype-system="http://java.sun.com/j2ee/dtds/web-jsptaglibrary_1_1.dtd"/>
   <xsl:strip-space elements="taglib tag attribute"/>

  <!-- Process an entire tag library -->
  <xsl:template match="taglib">
    <taglib>
      <xsl:if test="tlibversion">
        <tlibversion><xsl:value-of select="tlibversion"/></tlibversion>
      </xsl:if>
      <xsl:if test="jspversion">
        <jspversion><xsl:value-of select="jspversion"/></jspversion>
      </xsl:if>
      <xsl:if test="shortname">
        <shortname><xsl:value-of select="shortname"/></shortname>
      </xsl:if>
      <xsl:if test="uri">
        <uri><xsl:value-of select="uri"/></uri>
      </xsl:if>
      <xsl:apply-templates select="tag"/>
    </taglib>
  </xsl:template>

  <!-- Process an individual tag -->
  <xsl:template match="tag">
    <tag>
      <xsl:if test="name">
        <name><xsl:value-of select="name"/></name>
      </xsl:if>
      <xsl:if test="tagclass">
        <tagclass><xsl:value-of select="tagclass"/></tagclass>
      </xsl:if>
      <xsl:if test="teiclass">
        <teiclass><xsl:value-of select="teiclass"/></teiclass>
      </xsl:if>
      <xsl:if test="bodycontent">
        <bodycontent><xsl:value-of select="bodycontent"/></bodycontent>
      </xsl:if>
      <xsl:apply-templates select="attribute"/>
    </tag>
  </xsl:template>

  <!-- Process an individual tag attribute -->
  <xsl:template match="attribute">
    <attribute>
      <xsl:if test="name">
        <name><xsl:value-of select="name"/></name>
      </xsl:if>
      <xsl:if test="required">
        <required><xsl:value-of select="required"/></required>
      </xsl:if>
      <xsl:if test="rtexprvalue">
        <rtexprvalue><xsl:value-of select="rtexprvalue"/></rtexprvalue>
      </xsl:if>
    </attribute>
  </xsl:template>

  <!-- Skip irrelevant details -->
  <xsl:template match="properties"/>

</xsl:stylesheet>
