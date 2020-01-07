import { platformBrowser } from '@angular/platform-browser';
import { ProdConfig } from './blocks/config/prod.config';
import { PocAppModuleNgFactory } from '../../../../build/aot/src/main/webapp/app/app.module.ngfactory';

ProdConfig();

platformBrowser().bootstrapModuleFactory(PocAppModuleNgFactory)
.then((success) => console.log(`Application started`))
.catch((err) => console.error(err));
