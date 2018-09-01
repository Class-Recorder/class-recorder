import { Component } from '@angular/core';

import { ServerIpPage } from '../server-ip/server-ip';
import { RecordPage } from '../record/record';

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root = ServerIpPage;
  tab2Root = RecordPage;

  constructor() {

  }
}
