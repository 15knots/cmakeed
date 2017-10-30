<?xml version="1.0" encoding="UTF-8"?>
<!--
  transforms documentation on cmake properties into extension point description code for CmakeEd
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="UTF-8" method="xml" media-type="text/xml" omit-xml-declaration="yes" />

  <!-- all programming languages that cmake knows about.
    See https://stackoverflow.com/a/44477728 -->
  <xsl:variable name="languagesLiteral">
    <lang>ASM</lang>
    <lang>ASM-ATT</lang>
    <lang>ASM-MASM</lang>
    <lang>ASM-NASM</lang>
    <lang>C</lang>
    <lang>CSharp</lang>
    <lang>CUDA</lang>
    <lang>CXX</lang>
    <lang>Fortran</lang>
    <lang>Java</lang>
    <lang>RC</lang>
    <lang>Swift</lang>
  </xsl:variable>
  <xsl:variable name="languages" select="document('')//xsl:variable[@name='languagesLiteral']/lang/text()" />

  <xsl:template match="/">
    <xsl:if test="not(function-available('ends-with'))">
      <xsl:message terminate="yes" ><xsl:value-of select="'XPATH 2 is required to process this file!'" /></xsl:message>
    </xsl:if>

    <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="/document/section">
    <xsl:variable name="name" select="title/text()" />
    <!-- assume the first paragraph element contains the description -->
    <xsl:variable name="descr" select="paragraph[1][text()]" />
    <!-- first word in description marks deprecated properties -->
    <xsl:variable name="deprecated" select="starts-with($descr, 'Deprecated.')" />

    <!-- expand names like '&lt;LANG&gt;_*' for all languages -->
    <xsl:analyze-string regex="(&lt;LANG&gt;)([A-Z_]*[A-Z])"
      select="$name">
      <xsl:matching-substring>
        <!--
        <xsl:message terminate="no" select="concat('MATCH: #', $name, '#')" />
        -->
        <xsl:for-each select="$languages">
          <xsl:call-template name="property">
            <xsl:with-param name="name" select="concat(.,regex-group(2))" />
            <xsl:with-param name="descr" select="$descr" />
            <xsl:with-param name="deprecated" select="$deprecated" />
          </xsl:call-template>
        </xsl:for-each>
      </xsl:matching-substring>
      <xsl:non-matching-substring>
        <xsl:call-template name="property">
          <xsl:with-param name="name" select="$name" />
          <xsl:with-param name="descr" select="$descr" />
          <xsl:with-param name="deprecated" select="$deprecated" />
        </xsl:call-template>
      </xsl:non-matching-substring>
    </xsl:analyze-string>
  </xsl:template>

    <!-- constructs an element for cmakeed:
    <property name=".." desc=".." deprecated=".." />
  -->
  <xsl:template name="property">
    <xsl:param name="name" />
    <xsl:param name="descr" />
    <xsl:param name="deprecated" />

    <xsl:message terminate="no" select="concat('property: ', $name)" />
    <!--
    -->
    <xsl:element name="property">
      <xsl:attribute name="name" select="$name" />
      <xsl:attribute name="desc" select="normalize-space($descr)" />
      <xsl:if test="$deprecated">
        <xsl:attribute name="deprecated" select="'true'" />
      </xsl:if>
    </xsl:element>
    <xsl:value-of select="'&#10;'" />
  </xsl:template>

  <!-- suppress printing of empty lines -->
  <xsl:template match="@*|node()">
    <xsl:apply-templates select="@*|node()" />
  </xsl:template>
</xsl:stylesheet>