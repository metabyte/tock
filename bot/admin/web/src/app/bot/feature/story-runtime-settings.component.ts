/*
 * Copyright (C) 2017/2020 e-voyageurs technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {Component, OnInit} from "@angular/core";
import {BotService} from "../bot-service";

export class RuntimeStorySettings {
  name: string;
  type: string;

  constructor(name: string, type: string) {
    this.name = name;
    this.type = type;
  }
}

@Component({
  selector: 'tock-story-runtime-settings',
  templateUrl: './story-runtime-settings.component.html',
  styleUrls: ['./story-runtime-settings.component.css']
})
export class StoryRuntimeSettingsComponent implements OnInit {
  displayedColumns: string[] = ['storyType', 'storyName'];
  stories: RuntimeStorySettings[];

  constructor(private botService: BotService) {
  }

  ngOnInit(): void {
    this.stories = new Array<RuntimeStorySettings>();
    this.botService.findRuntimeStorySettings().subscribe(
      this.buildAndAddSettings()
    );
  }

  private buildAndAddSettings() {
    return stories => {
      this.stories = stories.map(story => {
        new RuntimeStorySettings(story.name, story.storyId);
      })
    };
  }
}