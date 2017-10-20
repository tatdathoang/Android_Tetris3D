/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.tdh.Sup;
import android.content.Context;
import android.opengl.GLSurfaceView;


public class GLGameSurfaceView extends GLSurfaceView
{
	private GameRenderer mRenderer;
    private GameOverlay overlay;
	
	public GLGameSurfaceView(Context context,GameOverlay overlay) 
	{
		super(context);
       
        this.overlay = overlay;
        
        if (this.mRenderer!=null)
        {
        	this.mRenderer = null;
        }

        //Optical. Dependent on Device which has or has no GPU
        this.getHolder().setType(android.view.SurfaceHolder.SURFACE_TYPE_GPU);
        
        this.mRenderer = new GameRenderer(context,overlay,overlay.getOptions());
		
		this.setRenderer(mRenderer);
	
		setFocusableInTouchMode(true);
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		mRenderer.onPause();   
	}
    
	@Override
    public void onResume()
    {
    	super.onResume();
    	mRenderer.onResume();
    }

    public void initGame(int gameType)
    {
        switch(gameType)
        {
                case GameRenderer.VIEW_INTRO:
                	this.mRenderer.setViewType(GameRenderer.VIEW_INTRO);
                    overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
                    overlay.setDrawType(GameOverlay.DRAW_NORMAL);
                    break;
                case GameRenderer.VIEW_GAME: 
                	this.mRenderer.setViewType(GameRenderer.VIEW_GAME);            
                    overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_GAME);
                    overlay.getOptions().setGame(new TetricGameData()); 
                    
                    overlay.getOptions().getGame().setLevel(overlay.getOptions().getStartingLevel());
                    overlay.setLevel(overlay.getOptions().getGame().getLevelName());
                    overlay.getOptions().getGame().initGame(overlay.getOptions().getStartingLevel());
                    
                    overlay.setDrawType(GameOverlay.DRAW_NORMAL);
                    break;                 
        }	
        this.mRenderer.reinit();
    }

    public GameRenderer getRenderer()
    {
    	return this.mRenderer;
    }   
    
    public GameOverlay getOverlay()
    {
            return overlay;
    }	
}
