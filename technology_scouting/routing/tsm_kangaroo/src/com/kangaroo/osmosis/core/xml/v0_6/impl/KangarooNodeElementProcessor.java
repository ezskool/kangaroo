package com.kangaroo.osmosis.core.xml.v0_6.impl;

/*
 * This file was modified for the kangaroo project to meet demands.
 * - the formerly used Node was exchanged with the 
 *   KangarooOSMNode.
 * 
 * Former package definition was
 * package com.bretth.osmosis.core.xml.v0_6.impl;
 * 
 */

import org.xml.sax.Attributes;

import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.domain.common.TimestampContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.core.xml.common.BaseElementProcessor;
import org.openstreetmap.osmosis.core.xml.common.ElementProcessor;
import org.openstreetmap.osmosis.core.xml.v0_6.impl.EntityElementProcessor;
import org.openstreetmap.osmosis.core.xml.v0_6.impl.NodeElementProcessor;
import org.openstreetmap.osmosis.core.xml.v0_6.impl.TagElementProcessor;
import org.openstreetmap.osmosis.core.xml.v0_6.impl.TagListener;
import org.openstreetmap.osmosis.core.OsmosisRuntimeException;

import android.util.Log;

import com.kangaroo.osmosis.core.domain.v0_6.KangarooOSMNode;


/**
 * Provides an element processor implementation for a node.
 * 
 * @author Brett Henderson
 */
public class KangarooNodeElementProcessor extends NodeElementProcessor implements TagListener {
	private static final String ELEMENT_NAME_TAG = "tag";
	private static final String ATTRIBUTE_NAME_ID = "id";
	private static final String ATTRIBUTE_NAME_TIMESTAMP = "timestamp";
	private static final String ATTRIBUTE_NAME_USER = "user";
	private static final String ATTRIBUTE_NAME_USERID = "uid";
	private static final String ATTRIBUTE_NAME_CHANGESET_ID = "changeset";
	private static final String ATTRIBUTE_NAME_VERSION = "version";
	private static final String ATTRIBUTE_NAME_LATITUDE = "lat";
	private static final String ATTRIBUTE_NAME_LONGITUDE = "lon";
	
	private TagElementProcessor tagElementProcessor;
	private Node node;
	
	
	/**
	 * Creates a new instance.
	 * 
	 * @param parentProcessor
	 *            The parent of this element processor.
	 * @param sink
	 *            The sink for receiving processed data.
	 * @param enableDateParsing
	 *            If true, dates will be parsed from xml data, else the current
	 *            date will be used thus saving parsing time.
	 */
	public KangarooNodeElementProcessor(BaseElementProcessor parentProcessor, Sink sink, boolean enableDateParsing) {
		super(parentProcessor, sink, enableDateParsing);
		
		tagElementProcessor = new TagElementProcessor(this, this);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void begin(Attributes attributes) {
		long id;
		String sversion;
		int version;
		TimestampContainer timestampContainer;
		String rawUserId;
		String rawUserName;
		OsmUser user;
		long changesetId;
		double latitude;
		double longitude;
		
		id = Long.parseLong(attributes.getValue(ATTRIBUTE_NAME_ID));
		sversion = attributes.getValue(ATTRIBUTE_NAME_VERSION);
		if (sversion == null) {
			throw new OsmosisRuntimeException("Node " + id
					+ " does not have a version attribute as OSM 0.6 are required to have.  Is this a 0.5 file?");
		} else {
			version = Integer.parseInt(sversion);
		}
		timestampContainer = createTimestampContainer(attributes.getValue(ATTRIBUTE_NAME_TIMESTAMP));
		rawUserId = attributes.getValue(ATTRIBUTE_NAME_USERID);
		rawUserName = attributes.getValue(ATTRIBUTE_NAME_USER);
		changesetId = buildChangesetId(attributes.getValue(ATTRIBUTE_NAME_CHANGESET_ID));
		latitude = Double.parseDouble(attributes.getValue(ATTRIBUTE_NAME_LATITUDE));
		longitude = Double.parseDouble(attributes.getValue(ATTRIBUTE_NAME_LONGITUDE));
		
		user = buildUser(rawUserId, rawUserName);
		
		/* this was formerly 'new Node(' */
		node = new KangarooOSMNode(id, version, timestampContainer, user, changesetId, latitude, longitude);

	}
	
	
	/**
	 * Retrieves the appropriate child element processor for the newly
	 * encountered nested element.
	 * 
	 * @param uri
	 *            The element uri.
	 * @param localName
	 *            The element localName.
	 * @param qName
	 *            The element qName.
	 * @return The appropriate element processor for the nested element.
	 */
	@Override
	public ElementProcessor getChild(String uri, String localName, String qName) {
		if (ELEMENT_NAME_TAG.equals(qName)) {
			return tagElementProcessor;
		}
		
		return super.getChild(uri, localName, qName);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void end() {
		getSink().process(new NodeContainer(node));
	}
	
	
	/**
	 * This is called by child element processors when a tag object is
	 * encountered.
	 * 
	 * @param tag
	 *            The tag to be processed.
	 */
	public void processTag(Tag tag) {
		node.getTags().add(tag);
	}
}
