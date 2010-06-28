<?xml version="1.0" encoding="UTF-8"?>

<!--
Document: hover.xsl
Author: illbagna
Description: XSL stylesheet for the retreival of the encoded document's content.
-->

<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns="http://www.w3.org/1999/xhtml"
  xmlns:rgb="http://rgb"
  exclude-result-prefixes="xsl xs rgb">
 


  <!--xsl:strip-space elements="*"/-->


  <xsl:param name="service" required="yes"/>
  <xsl:param name="witNum" required="no"/>
  <xsl:param name="transcription" required="no"/>




  <!-- variables -->
  <!-- newline -->
  <xsl:variable name="newline">
     <br />
  </xsl:variable>
  <!-- blank -->
  <xsl:variable name="blank">
     <xsl:text> </xsl:text>
  </xsl:variable>
  <!-- comma -->
  <xsl:variable name="comma">
     <xsl:text>, </xsl:text>
  </xsl:variable>
  <!-- minus -->
  <xsl:variable name="minus">
     <xsl:text>-</xsl:text>
  </xsl:variable>
  <!-- brackets -->
  <xsl:variable name="open_bracket">
     <xsl:text>(</xsl:text>
  </xsl:variable>
  <xsl:variable name="close_bracket">
     <xsl:text>)</xsl:text>
  </xsl:variable>
  <!-- squared brackets -->
  <xsl:variable name="open_squared_bracket">
     <xsl:text>[</xsl:text>
  </xsl:variable>
  <xsl:variable name="close_squared_bracket">
     <xsl:text>]</xsl:text>
  </xsl:variable>
  <!-- result documents -->
  <!-- <xsl:variable name="file_content" select="concat('./','content','.html')"/>
  <xsl:variable name="file_note" select="concat('./','note','.html')"/> -->




  <xsl:template match="/">
	<!--
	the tree node in which the body may be nested
	-->
	<xsl:variable name="document_content" as="item()*">
	  <xsl:choose>

		<!-- in case the teiHeader has been defined, take the next node with all its descendants -->
		<xsl:when test="//*:teiHeader">
		  <xsl:for-each select="//element()[preceding-sibling::*:teiHeader]">
			<xsl:sequence select="."/> <!-- take this element with all its descendants -->
			<xsl:for-each select=".//element()">
			  <xsl:sequence select="."/>
			</xsl:for-each>
		  </xsl:for-each>
		</xsl:when>

		<!-- in case the teiHeader isn't present, take everithing -->
		<xsl:otherwise>
		  <xsl:for-each select="//element()">
			<xsl:sequence select="."/>
		  </xsl:for-each>
		</xsl:otherwise>

	  </xsl:choose>
	</xsl:variable>
	<!--
	DEBUG
	<xsl:text>DOC CONTENT:</xsl:text>
	<xsl:value-of select="$document_content/name()" separator=" "/>
	<xsl:value-of select="'&#xA;'"/>
	-->




	<!--
	note
	-->
	<xsl:variable name="content_note" as="element()*">
	  <xsl:for-each select="//*:note[not(ancestor-or-self::*:teiHeader)]">
		<xsl:sequence select="."/>
		<xsl:for-each select=".//element()">
		  <xsl:sequence select="."/>
		</xsl:for-each>
	  </xsl:for-each>
	</xsl:variable>
	<!--
	DEBUG
	<xsl:text>NOT CONTENT:</xsl:text>
	<xsl:value-of select="$content_note/name()" separator=" "/>
	<xsl:value-of select="'&#xA;'"/>
	-->




	<!--
	the title:
	possible locations for the title
	-->
	<!-- <head> -->
	<xsl:variable name="title_head" as="element()*">
	  <xsl:for-each select="//*:head[not(@type)
		and not(ancestor-or-self::*:note)
		and not(ancestor-or-self::*:witList)
		and not(ancestor-or-self::*:teiHeader)]">
		<xsl:sequence select="."/> <!-- take this element with all its descendants -->
		<xsl:for-each select=".//element()">
		  <xsl:sequence select="."/>
		</xsl:for-each>
	  </xsl:for-each>
	</xsl:variable>

	<!-- @type -->
	<xsl:variable name="title_type" as="element()*">
	  <xsl:for-each select="//*:head[@type
		and not(ancestor-or-self::*:note)
		and not(ancestor-or-self::*:witList)
		and not(ancestor-or-self::*:teiHeader)]
		|
		//*:title[@type
		and not(ancestor-or-self::*:note)
		and not(ancestor-or-self::*:witList)
		and not(ancestor-or-self::*:teiHeader)]">
		<xsl:sequence select="."/> <!-- take this element with all its descendants -->
		<xsl:for-each select=".//element()">
		  <xsl:sequence select="."/>
		</xsl:for-each>
	  </xsl:for-each>
	</xsl:variable>

	<!-- locating the title -->
	<xsl:variable name="content_title" as="element()*">
	  <xsl:choose>
		<xsl:when test="boolean(not(empty($title_head)))">
		  <xsl:sequence select="$title_head"/>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:sequence select="$title_type"/>
		</xsl:otherwise>
	  </xsl:choose>
	</xsl:variable>
	<!--
	DEBUG
	<xsl:text>TIT CONTENT:</xsl:text>
	<xsl:value-of select="$content_title/name()" separator=" "/>
	<xsl:value-of select="'&#xA;'"/>
	-->




	<!--
	the body:
	possible locations
	-->
	<!-- lg / l -->
	<xsl:variable name="content_lg" as="element()*">
	  <xsl:for-each select="//*:lg[
		not(ancestor-or-self::*:note)
		and not(ancestor-or-self::*:teiHeader)
		and not(ancestor-or-self::*:witList)
		and (empty(ancestor-or-self::node() intersect $content_title))
		and (empty(descendant-or-self::node() intersect $content_title))]">
		<xsl:sequence select="."/> <!-- take this element with all its descendants -->
		<xsl:for-each select=".//element()">
		  <xsl:sequence select="."/>
		</xsl:for-each>
	  </xsl:for-each>
	</xsl:variable>

	<!-- <div*> -->
	<xsl:variable name="content_div" as="element()*">
	  <xsl:for-each select="//element()[
		starts-with(name(), 'div')
		and not(ancestor-or-self::*:note)
		and not(ancestor-or-self::*:teiHeader)
		and not(ancestor-or-self::*:witList)
		and (empty(ancestor::node() intersect $content_title))
		and (empty(ancestor::node() intersect $content_lg))]">
		<xsl:sequence select="."/> <!-- take this element with all its descendants -->
		<xsl:for-each select=".//element()">
		  <xsl:sequence select="."/>
		</xsl:for-each>
	  </xsl:for-each>
	</xsl:variable>

	<!-- <body> -->
	<xsl:variable name="content_p" as="element()*">
	  <xsl:for-each select="//*:body[
		not(ancestor-or-self::*:note)
		and not(ancestor-or-self::*:teiHeader)
		and not(ancestor-or-self::*:witList)
		and (empty(ancestor::node() intersect $content_title))
		and (empty(ancestor::node() intersect $content_lg))
		and (empty(ancestor::node() intersect $content_div))]">
		<xsl:sequence select="."/> <!-- take this element with all its descendants -->
		<xsl:for-each select=".//element()">
		  <xsl:sequence select="."/>
		</xsl:for-each>
	  </xsl:for-each>
	</xsl:variable>

	<!-- locating the body content -->
	<xsl:variable name="content_body" as="element()*">
	  <xsl:choose>
		<xsl:when test="boolean(not(empty($content_p)))">
		  <xsl:sequence select="$content_p"/>
		</xsl:when>
		<xsl:when test="boolean(not(empty($content_div)))">
		  <xsl:sequence select="$content_div"/>
		</xsl:when>
		<xsl:otherwise>
		  <xsl:sequence select="$content_lg"/>
		</xsl:otherwise>
	  </xsl:choose>
	</xsl:variable>
	<!--
	DEBUG
	<xsl:text>BOD CONTENT:</xsl:text>
	<xsl:value-of select="$content_body/name()" separator=" "/>
	<xsl:value-of select="'&#xA;'"/>

	<xsl:text>DIV CONTENT:</xsl:text>
	<xsl:value-of select="$content_div/name()" separator=" "/>
	<xsl:value-of select="'&#xA;'"/>

	<xsl:text>PPP CONTENT:</xsl:text>
	<xsl:value-of select="$content_p/name()" separator=" "/>
	<xsl:value-of select="'&#xA;'"/>
	-->




	<!--
	service lookup
	-->
	<xsl:choose>

	  <!-- export the content of the encoded document to the application -->
	  <xsl:when test="matches($service, 'visualizzazione')">
		<!-- <xsl:result-document method="xhtml" indent="yes" omit-xml-declaration="yes" href="{$file_content}" encoding="UTF-8"> -->
                    <xsl:result-document method="xhtml" indent="yes" omit-xml-declaration="yes" encoding="UTF-8">
		  <html>
			<head/>
			<body>
			  <xsl:apply-templates select="$document_content[
				not(empty(node() intersect $content_title))
				or not(empty(node() intersect $content_body))] [1]" mode="visualizzazione">
				<xsl:with-param name="content_title" select="$content_title" tunnel="yes"/>
				<xsl:with-param name="content_body" select="$content_body" tunnel="yes"/>
			  </xsl:apply-templates>
			</body>
		  </html>
		</xsl:result-document>
	  </xsl:when>

	  <!-- export the content of the encoded document in plain text for the service "concordanze" -->
	  <xsl:when test="matches($service, 'concordanze')">
		<xsl:result-document method="text" indent="no" include-content-type="no" encoding="UTF-8">
		  <xsl:apply-templates select="$document_content[
			not(empty(node() intersect $content_title))
			or not(empty(node() intersect $content_body))] [1]" mode="concordanze">
			<xsl:with-param name="content_title" select="$content_title" tunnel="yes"/>
			<xsl:with-param name="content_body" select="$content_body" tunnel="yes"/>
		  </xsl:apply-templates>
		</xsl:result-document>
	  </xsl:when>

	  <!-- export the content of the notes from the encoded document to the application -->
	  <xsl:when test="matches($service, 'note')">
		<!-- <xsl:result-document method="xhtml" indent="yes" omit-xml-declaration="yes" href="{$file_note}" encoding="UTF-8"> -->
                    <xsl:result-document method="xhtml" indent="yes" omit-xml-declaration="yes" encoding="UTF-8">
		  <html>
			<head/>
			<body>
			  <xsl:apply-templates select="$content_note[matches(name(), 'note')]" mode="note">
				<xsl:with-param name="content_title" select="$content_title" tunnel="yes"/>
				<xsl:with-param name="content_body" select="$content_body" tunnel="yes"/>
			  </xsl:apply-templates>
			</body>
		  </html>
		</xsl:result-document>
	  </xsl:when>

	  <!-- default -->
	  <xsl:otherwise/>

	</xsl:choose>
  </xsl:template>




  <!-- br -->
  <!-- mode "visualizzazione / note" -->
  <xsl:template match="*:br" mode="visualizzazione note">
	<xsl:copy-of select="$newline"/>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:br" mode="concordanze">
	<xsl:text>&#xA;</xsl:text>
  </xsl:template>




  <!-- head -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:head" mode="visualizzazione">
	<h3>
	  <xsl:apply-templates mode="visualizzazione"/>
	</h3>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:head" mode="concordanze">
	<xsl:apply-templates mode="concordanze"/>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:head" mode="note">
	<xsl:apply-templates mode="note"/>
  </xsl:template>




  <!-- body -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:body" mode="visualizzazione">
	<xsl:apply-templates mode="visualizzazione"/>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:body" mode="concordanze">
	<xsl:apply-templates mode="concordanze"/>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:body" mode="note">
	<xsl:apply-templates mode="visualizzazione"/>
  </xsl:template>




  <!-- div -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="element()[starts-with(name(), 'div')]" mode="visualizzazione">
	<xsl:param name="content_body" tunnel="yes" required="yes"/>

	<xsl:if test="not(empty(node() intersect $content_body))">
	  <!-- a new section ? -->
	  <xsl:if test="node()[@key]">
		<span id="nuovo_argomento">
		  <h4>
			<xsl:copy-of select="$open_squared_bracket"/>
			<xsl:value-of select="@key"/>
			<xsl:copy-of select="$close_squared_bracket"/>
		  </h4>
		</span>
	  </xsl:if>
	  <xsl:apply-templates mode="visualizzazione"/>
	</xsl:if>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="element()[starts-with(name(), 'div')]" mode="concordanze">
	<xsl:param name="content_body" tunnel="yes" required="yes"/>

	<xsl:if test="not(empty(node() intersect $content_body))">
	  <xsl:apply-templates mode="concordanze"/>
	</xsl:if>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="element()[starts-with(name(), 'div')]" mode="note">
	<xsl:apply-templates/>
  </xsl:template>




  <!-- opener / closer -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:opener | *:closer" mode="visualizzazione">
	<p>
	  <xsl:apply-templates mode="visualizzazione"/>
	</p>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:opener | *:closer" mode="concordanze">
	<xsl:apply-templates mode="concordanze"/>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:opener | *:closer" mode="note">
	<p>
	  <xsl:apply-templates mode="note"/>
	</p>
  </xsl:template>




  <!-- lb -->
  <!-- mode "visualizzazione" / "note" -->
  <xsl:template match="*:lb" mode="visualizzazione note">
	<xsl:choose>
	  <!-- w/ line numbers -->
	  <xsl:when test=".[@n]">
		<xsl:if test="preceding-sibling::node()[matches(name(), ./name())]">
		  <xsl:copy-of select="$newline"/>
		</xsl:if>
		
		<span id="lnum">
		  <xsl:value-of select="./@n"/>
		  <xsl:copy-of select="$blank"/>
		</span>
	  </xsl:when>
	  
	  <!-- w/out line numbers -->
	  <xsl:otherwise>
		<xsl:copy-of select="$newline"/>
	  </xsl:otherwise>
	</xsl:choose>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:lb" mode="concordanze">
	<xsl:text>&#xA;</xsl:text>
  </xsl:template>




  <!-- p / lg -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:p | *:lg" mode="visualizzazione">
	<p>
	  <xsl:apply-templates mode="visualizzazione"/>
	</p>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:p" mode="concordanze">
	<xsl:apply-templates mode="concordanze"/>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:p | *:lg" mode="note">
	<p>
	  <xsl:apply-templates mode="note"/>
	</p>
  </xsl:template>




  <!-- l -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:l" mode="visualizzazione">
	<xsl:if test="preceding-sibling::*:l">
	  <xsl:copy-of select="$newline"/>
	</xsl:if>

	<xsl:if test=".[@n]">
	  <span id="lnum">
		<xsl:value-of select="./@n"/>
		<xsl:copy-of select="$blank"/>
	  </span>
	</xsl:if>
	<xsl:apply-templates mode="visualizzazione"/>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:l" mode="concordanze">
	<xsl:if test="preceding-sibling::*:l">
	  <xsl:text>&#xA;</xsl:text>
	</xsl:if>
	<xsl:apply-templates mode="concordanze"/>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:l" mode="note">
	<xsl:if test="preceding-sibling::*:l">
	  <xsl:copy-of select="$newline"/>
	</xsl:if>

	<xsl:if test=".[@n]">
	  <span id="lnum">
		<xsl:value-of select="./@n"/>
		<xsl:copy-of select="$blank"/>
	  </span>
	</xsl:if>

	<xsl:apply-templates mode="note"/>
  </xsl:template>




  <!-- app -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:app" mode="visualizzazione">
	<xsl:apply-templates mode="visualizzazione"/>

	<xsl:if test="position() != last()">
	  <xsl:copy-of select="$blank"/>
	</xsl:if>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:app" mode="concordanze">
	<xsl:apply-templates mode="concordanze"/>

	<xsl:if test="position() != last()">
	  <xsl:copy-of select="$blank"/>
	</xsl:if>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:app" mode="note">
	<xsl:apply-templates mode="note"/>

	<xsl:if test="position() != last()">
	  <xsl:copy-of select="$blank"/>
	</xsl:if>
  </xsl:template>




  <!-- rdg -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:rdg" mode="visualizzazione">
	<xsl:if test="contains(@wit, $witNum)">
	  <xsl:if test="(position() &gt; 1) and (position() != last())">
		<xsl:copy-of select="$blank"/>
	  </xsl:if>
	  <xsl:apply-templates mode="visualizzazione"/>
	</xsl:if>
  </xsl:template>
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:rdg" mode="concordanze">
	<xsl:if test="contains(@wit, $witNum)">
	  <xsl:if test="(position() &gt; 1) and (position() != last())">
		<xsl:copy-of select="$blank"/>
	  </xsl:if>
	  <xsl:apply-templates mode="concordanze"/>
	</xsl:if>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:rdg" mode="note">
	<xsl:if test="contains(@wit, $witNum)">
	  <xsl:if test="(position() &gt; 1) and (position() != last())">
		<xsl:copy-of select="$blank"/>
	  </xsl:if>
	  <xsl:apply-templates mode="note"/>
	</xsl:if>
  </xsl:template>




  <!-- hi -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:hi" mode="visualizzazione">
	<xsl:choose>
	  <xsl:when test="not(@rend) or not(contains(@rend, 'underline'))">
		<xsl:apply-templates mode="visualizzazione"/>
	  </xsl:when>
	  <xsl:otherwise>
		<u>
		  <xsl:apply-templates mode="visualizzazione"/>
		</u>
	  </xsl:otherwise>
	</xsl:choose>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:hi" mode="concordanze">
	<xsl:apply-templates mode="concordanze"/>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:hi" mode="note">
	<xsl:choose>
	  <xsl:when test="not(@rend) or not(contains(@rend, 'underline'))">
		<xsl:apply-templates mode="note"/>
	  </xsl:when>
	  <xsl:otherwise>
		<u>
		  <xsl:apply-templates mode="note"/>
		</u>
	  </xsl:otherwise>
	</xsl:choose>
  </xsl:template>




  <!-- del -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:del" mode="visualizzazione">
	<del>
	  <xsl:apply-templates mode="visualizzazione"/>
	</del>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:del" mode="concordanze"/>
  <!-- mode "note" -->
  <xsl:template match="*:del" mode="note">
	<del>
	  <xsl:apply-templates mode="note"/>
	</del>
  </xsl:template>




  <!-- add -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:add" mode="visualizzazione">
	<em>
	  <xsl:apply-templates mode="visualizzazione"/>
	</em>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:add" mode="concordanze">
	<xsl:apply-templates mode="concordanze"/>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:add" mode="note">
	<em>
	  <xsl:apply-templates mode="note"/>
	</em>
  </xsl:template>




  <!-- argument -->
  <!-- mode "visualizzazione" / "note" -->
  <xsl:template match="*:argument | *:bibl" mode="visualizzazione note">
	<p>
	  <span id="legend">
		<xsl:value-of select="@type"/>
		<xsl:copy-of select="$blank"/>
	  </span>
	  <xsl:value-of select="."/>
	</p>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:argument | *:bibl" mode="concordanze">
	<xsl:value-of select="."/>
  </xsl:template>




  <!-- lem -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:lem" mode="visualizzazione">
	<xsl:param name="content_title" tunnel="yes" required="yes"/>
	
	<xsl:if test="ancestor::node()[not(empty(node() intersect $content_title))]">
	  <span id="legend">
		<xsl:text>titolo</xsl:text>
		<xsl:copy-of select="$blank"/>
	  </span>
	</xsl:if>

	<xsl:apply-templates mode="visualizzazione"/>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:lem" mode="concordanze">
	<xsl:apply-templates mode="concordanze"/>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:lem" mode="note">
	<xsl:param name="content_title" tunnel="yes" required="yes"/>
	
	<xsl:if test="ancestor::node()[not(empty(node() intersect $content_title))]">
	  <span id="legend">
		<xsl:text>titolo</xsl:text>
		<xsl:copy-of select="$blank"/>
	  </span>
	</xsl:if>

	<xsl:apply-templates mode="note"/>
  </xsl:template>




  <!-- note -->
  <!-- mode "visualizzazione" / "concordanze" -->
  <xsl:template match="*:note" mode="visualizzazione concordanze"/>
  <!-- mode "note" -->
  <xsl:template match="*:note" mode="note">
	<xsl:param name="content_body" tunnel="yes"/>

	<!--
	<xsl:choose>

	  <xsl:when test="matches($transcription, 'diplomatica') or matches($transcription, 'interpretativa') or matches($transcription, 'critica')">
		<xsl:if test="position() = 1">
		</xsl:if>
	  </xsl:when>

	  <xsl:otherwise>
	  -->
		<xsl:choose>

		  <xsl:when test="//*:witList">
			<xsl:choose>
			  <xsl:when test="node()[(ancestor::node()[contains(@wit, $witNum)]) or (not(ancestor::node()[@wit]))]">
				<xsl:if test="ancestor::node()[@n]">
				  <span class="lnum">
					<xsl:value-of select="ancestor::node()[@n][1]/@n"/>
					<xsl:copy-of select="$blank"/>
				  </span>
				</xsl:if>
				<span class="note_type">
				  <b><xsl:value-of select="@type"/></b>
				</span>

				<p>
				  <xsl:apply-templates mode="note"/>
				</p>
			  </xsl:when>

			  <xsl:otherwise/>

			</xsl:choose>
		  </xsl:when>

		  <xsl:otherwise>
			<xsl:value-of select="//*:anchor[ancestor::node()[@n][1]/@n]/@id"/>
			<xsl:value-of select="@target"/>
			<xsl:value-of select="//attribute(xml:id)"/>
				<span class="note_type">
				  <b><xsl:value-of select="@type"/></b>
				</span>

				<p>
				  <xsl:apply-templates mode="note"/>
				</p>
		  </xsl:otherwise>
		</xsl:choose>
		<!--
	  </xsl:otherwise>

	</xsl:choose>
	-->
  </xsl:template>




  <!-- choice -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:choice" mode="visualizzazione">
	<xsl:apply-templates mode="visualizzazione"/>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:choice" mode="concordanze">
	<xsl:apply-templates mode="concordanze"/>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:choice" mode="note">
	<xsl:apply-templates mode="note"/>
  </xsl:template>




  <!-- orig -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:orig" mode="visualizzazione">
	<xsl:choose>
	  <!-- diplomatica -->
	  <xsl:when test="matches($transcription, 'diplomatica')">
		<xsl:apply-templates mode="visualizzazione"/>
	  </xsl:when>
	  <!--  default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:orig" mode="concordanze">
	<xsl:choose>
	  <!-- diplomatica -->
	  <xsl:when test="matches($transcription, 'diplomatica')">
		<xsl:apply-templates mode="concordanze"/>
	  </xsl:when>
	  <!-- default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:orig" mode="note">
	<xsl:choose>
	  <!-- diplomatica -->
	  <xsl:when test="matches($transcription, 'diplomatica')">
		<xsl:apply-templates mode="note"/>
	  </xsl:when>
	  <!--  default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>




  <!-- reg -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:reg" mode="visualizzazione">
	<xsl:choose>
	  <!-- interpretativa -->
	  <xsl:when test="matches($transcription, 'interpretativa')">
		<xsl:choose>
		  <xsl:when test="(parent::*:choice) and (not(following-sibling::*:orig) and not(preceding-sibling::*:orig))"/>
		  <xsl:otherwise>
			<span class="regolarizzazione">
			  <xsl:apply-templates mode="visualizzazione"/>
			</span>
		  </xsl:otherwise>
		</xsl:choose>
	  </xsl:when>
	  <!--  default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:reg" mode="concordanze">
	<xsl:choose>
	  <!-- interpretativa -->
	  <xsl:when test="matches($transcription, 'interpretativa')">
		<xsl:choose>
		  <xsl:when test="(parent::*:choice) and (not(following-sibling::*:orig) and not(preceding-sibling::*:orig))"/>
		  <xsl:otherwise>
			<xsl:apply-templates mode="concordanze"/>
		  </xsl:otherwise>
		</xsl:choose>
	  </xsl:when>
	  <!-- default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:reg" mode="note">
	<xsl:choose>
	  <!-- interpretativa -->
	  <xsl:when test="matches($transcription, 'interpretativa')">
		<xsl:choose>
		  <xsl:when test="(parent::*:choice) and (not(following-sibling::*:orig) and not(preceding-sibling::*:orig))"/>
		  <xsl:otherwise>
			<span class="regolarizzazione">
			  <xsl:apply-templates mode="note"/>
			</span>
		  </xsl:otherwise>
		</xsl:choose>
	  </xsl:when>
	  <!--  default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>




  <!-- expan -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:expan" mode="visualizzazione">
	<xsl:choose>
	  <!-- interpretativa -->
	  <xsl:when test="matches($transcription, 'interpretativa')">
		<span class="espansione">
		  <xsl:copy-of select="$open_bracket"/>
		  <xsl:apply-templates mode="visualizzazion"/>
		  <xsl:copy-of select="$close_bracket"/>
		</span>
	  </xsl:when>
	  <!--  default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:expan" mode="concordanze">
	<xsl:choose>
	  <!-- interpretativa -->
	  <xsl:when test="matches($transcription, 'interpretativa')">
		<xsl:apply-templates mode="concordanze"/>
	  </xsl:when>
	  <!-- default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:expan" mode="note">
	<xsl:choose>
	  <!-- interpretativa -->
	  <xsl:when test="matches($transcription, 'interpretativa')">
		<span class="espansione">
		  <xsl:copy-of select="$open_bracket"/>
		  <xsl:apply-templates mode="note"/>
		  <xsl:copy-of select="$close_bracket"/>
		</span>
	  </xsl:when>
	  <!--  default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>




  <!-- abbr -->
  <!-- mode "visualizzazione" -->
  <xsl:template match="*:abbr" mode="visualizzazione">
	<xsl:choose>
	  <!-- diplomatica -->
	  <xsl:when test="matches($transcription, 'diplomatica')">
		<xsl:apply-templates mode="visualizzazione"/>
	  </xsl:when>
	  <!--  default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>
  <!-- mode "concordanze" -->
  <xsl:template match="*:abbr" mode="concordanze">
	<xsl:choose>
	  <!-- diplomatica -->
	  <xsl:when test="matches($transcription, 'diplomatica')">
		<xsl:apply-templates mode="concordanze"/>
	  </xsl:when>
	  <!-- default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>
  <!-- mode "note" -->
  <xsl:template match="*:abbr" mode="note">
	<xsl:choose>
	  <!-- diplomatica -->
	  <xsl:when test="matches($transcription, 'diplomatica')">
		<xsl:apply-templates mode="note"/>
	  </xsl:when>
	  <!--  default -->
	  <xsl:otherwise/>
	</xsl:choose>
  </xsl:template>

</xsl:stylesheet>
