/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.halyard.config.validate.v1.security;

import com.netflix.spinnaker.halyard.config.model.v1.node.Validator;
import com.netflix.spinnaker.halyard.config.model.v1.security.Authz;
import com.netflix.spinnaker.halyard.config.model.v1.security.GroupMembership;
import com.netflix.spinnaker.halyard.config.problem.v1.ConfigProblemSetBuilder;
import com.netflix.spinnaker.halyard.core.problem.v1.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;

@Component
public class AuthzValidator extends Validator<Authz> {

  GoogleRoleProviderValidator googleValidator = new GoogleRoleProviderValidator();

  GithubRoleProviderValidator githubValidator = new GithubRoleProviderValidator();

  FileRoleProviderValidator fileValidator = new FileRoleProviderValidator();

  @Override
  public void validate(ConfigProblemSetBuilder p, Authz z) {
    if (!z.isEnabled()) {
      return;
    }

    switch (z.getGroupMembership().getService()) {
      case GITHUB:
        githubValidator.validate(p, z.getGroupMembership().getGithub());
        break;
      case GOOGLE:
        googleValidator.validate(p, z.getGroupMembership().getGoogle());
        break;
      case FILE:
        fileValidator.validate(p, z.getGroupMembership().getFile());
      case EXTERNAL: // fallthrough
      default:
        break;
    }
  }
}
