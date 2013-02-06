/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2005-2011 Pentaho Corporation.  All rights reserved.
 */

package org.pentaho.reporting.designer.core.editor.report.layouting;

import org.pentaho.reporting.designer.core.editor.ReportRenderContext;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.layout.model.LogicalPageBox;
import org.pentaho.reporting.engine.classic.core.layout.output.ContentProcessingException;
import org.pentaho.reporting.engine.classic.core.layout.output.OutputProcessorMetaData;
import org.pentaho.reporting.engine.classic.core.wizard.DataSchema;

/**
 * Single-instance layouter for handling the layout computation for a single report.
 * This layouts the full report with all bands. The no-data-band will be layouted
 * in parallel to the itemband.
 */
public class ReportLayouter
{
  private LogicalPageBox logicalPageBox;
  private ReportRenderContext reportRenderContext;
  private long lastModCount;

  private DesignerExpressionRuntime runtime;

  public ReportLayouter(final ReportRenderContext reportRenderContext)
  {
    this.reportRenderContext = reportRenderContext;
    this.lastModCount = 0;
  }

  private DataSchema getDataSchema()
  {
    return this.reportRenderContext.getReportDataSchemaModel().getDataSchema();
  }

  public LogicalPageBox layout() throws ReportProcessingException, ContentProcessingException
  {
    try
    {
      final MasterReport report = reportRenderContext.getMasterReportElement();
      if (logicalPageBox != null && lastModCount == report.getChangeTracker())
      {
        return logicalPageBox;
      }

      final MasterReport masterReport = (MasterReport) report.derive(true);
      final DesignerOutputProcessor outputProcessor = initForReport(masterReport);

      final DesignerReportProcessor reportProcessor = new DesignerReportProcessor(masterReport, outputProcessor);
      reportProcessor.processReport();
      this.logicalPageBox = outputProcessor.getLogicalPage();
      lastModCount = report.getChangeTracker();
      return logicalPageBox;
    }
    finally
    {
      this.runtime = null;
    }
  }

  private DesignerOutputProcessor initForReport(final MasterReport masterReport)
  {
    final LayoutingContext context = new LayoutingContext(masterReport);
    this.runtime = context.getRuntime();
    this.runtime.setContentBase(masterReport.getContentBase());
    this.runtime.setDataSchema(getDataSchema());

    return context.getOutputProcessor();
  }

  public OutputProcessorMetaData getOutputProcessorMetaData()
  {
    final DesignerOutputProcessorMetaData designerOutputProcessorMetaData = new DesignerOutputProcessorMetaData();
    designerOutputProcessorMetaData.initialize(reportRenderContext.getMasterReportElement().getConfiguration());
    return designerOutputProcessorMetaData;
  }
}