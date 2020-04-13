/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhihu.matisse.internal.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseArray;
import androidx.loader.content.CursorLoader;
import com.zhihu.matisse.internal.entity.Album;
import com.zhihu.matisse.internal.entity.SelectionSpec;

import static com.zhihu.matisse.internal.entity.Album.ALBUM_ID_ALL;
import static com.zhihu.matisse.internal.entity.Album.ALBUM_NAME_ALL;

/**
 * Load all albums (grouped by bucket_id) into a single cursor.
 */
public class AlbumLoader_androidQ extends CursorLoader {
    public static final String COLUMN_COUNT = "count";
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String COLUMN_BUCKET_ID = "bucket_id";
    private static final String COLUMN_BUCKET_NAME = "bucket_display_name";
    private static final String[] COLUMNS = {
            MediaStore.Files.FileColumns._ID,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_NAME,
            MediaStore.MediaColumns.DATA,
            COLUMN_COUNT};
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_NAME,
            MediaStore.MediaColumns.DATA};

    // === params for showSingleMediaType: false ===
    private static final String SELECTION =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String[] SELECTION_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };
    // =============================================

    // === params for showSingleMediaType: true ===
    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        return new String[]{String.valueOf(mediaType)};
    }
    // =============================================

    private static final String BUCKET_ORDER_BY = "datetaken DESC";

    private AlbumLoader_androidQ(Context context, String selection, String[] selectionArgs) {
        super(context, QUERY_URI, PROJECTION, selection, selectionArgs, BUCKET_ORDER_BY);
    }

    public static CursorLoader newInstance(Context context) {
        String selection;
        String[] selectionArgs;
        if (SelectionSpec.getInstance().onlyShowImages()) {
            selection = SELECTION_FOR_SINGLE_MEDIA_TYPE;
            selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        } else if (SelectionSpec.getInstance().onlyShowVideos()) {
            selection = SELECTION_FOR_SINGLE_MEDIA_TYPE;
            selectionArgs = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        } else {
            selection = SELECTION;
            selectionArgs = SELECTION_ARGS;
        }
        return new AlbumLoader_androidQ(context, selection, selectionArgs);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = super.loadInBackground();
        MatrixCursor albumCursor = new MatrixCursor(COLUMNS);
        if (cursor == null) {
            return albumCursor;
        }

        int totalCount = cursor.getCount();
        boolean allAdded = false;
        SparseArray<Album> albums = new SparseArray<>();
        while (cursor.moveToNext()) {
            String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            int bucketId = cursor.getInt(cursor.getColumnIndex(COLUMN_BUCKET_ID));
            String bucketName = cursor.getString(cursor.getColumnIndex(COLUMN_BUCKET_NAME));
            if (!allAdded) {
                albumCursor.addRow(new String[]{ALBUM_ID_ALL, ALBUM_ID_ALL, ALBUM_NAME_ALL, filepath, String.valueOf(totalCount)});
                allAdded = true;
            }

            Album album = albums.get(bucketId);
            if (album == null) {
                album = new Album(bucketId + "", filepath, bucketName, 0);
                albums.append(bucketId, album);
            }
            album.addCaptureCount();
        }

        for (int i = 0; i < albums.size(); i++) {
            Album album = albums.valueAt(i);
            String id = album.getId();
            String name = album.getDisplayName(getContext());
            String coverPath = album.getCoverPath();
            String count = album.getCount() + "";
            albumCursor.addRow(new String[]{id, id, name, coverPath, count});
        }

        return albumCursor;
    }

    @Override
    public void onContentChanged() {
        // FIXME a dirty way to fix loading multiple times
    }
}