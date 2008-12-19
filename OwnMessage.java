/* This code is part of Freenet. It is distributed under the GNU General
 * Public License, version 2 (or at your option any later version). See
 * http://www.gnu.org/ for further details of the GPL. */
package plugins.Freetalk;

import java.util.Date;
import java.util.List;
import java.util.Set;

import plugins.Freetalk.exceptions.InvalidParameterException;

import freenet.keys.FreenetURI;

public final class OwnMessage extends Message {
	
	private boolean iWasInserted = false;

	public OwnMessage(Message newParentThread, Message newParentMessage, Set<Board> newBoards, Board newReplyToBoard, FTOwnIdentity newAuthor, String newTitle,
			Date newDate, int newIndex, String newText, List<Attachment> newAttachments) throws InvalidParameterException {
		super(generateRequestURI(newAuthor, newIndex),
			  (newParentThread == null ? null : newParentThread.getURI()),
			  (newParentMessage == null ? null : newParentMessage.getURI()),
			  newBoards, newReplyToBoard, newAuthor, newTitle, newDate, newText, newAttachments);
	}

	/* Override for synchronization */
	@Override
	public synchronized FreenetURI getURI() {
		return mURI;
	}
	
	/* Override for synchronization */
	@Override
	public synchronized String getID() {
		return mID;
	}

	public synchronized FreenetURI getInsertURI() {
		return generateURI(((FTOwnIdentity)mAuthor).getInsertURI(), mIndex);
	}
	
	/**
	 * Called when we detect a collision during insertion.
	 */
	public synchronized void incrementInsertIndex() {
		synchronized(OwnMessage.class) {
			mIndex = mMessageManager.getFreeMessageIndex((FTOwnIdentity)mAuthor);
			mURI = generateRequestURI(mAuthor, mIndex);
			mID = generateID(mURI);
			store();
		}
	}
	
	public synchronized boolean wasInserted() {
		return iWasInserted;
	}
	
	public synchronized void markAsInserted() {
		iWasInserted = true;
	}

}
